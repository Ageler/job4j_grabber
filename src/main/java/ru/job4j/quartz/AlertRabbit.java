package ru.job4j.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Date;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

@Slf4j
public class AlertRabbit {

    private Properties properties;

    private static Connection connection;
    private static final int PAGE = 3;

    public AlertRabbit(Properties properties, String filepath) {
        this.properties = properties;
        initConnection(filepath);
    }

    public void initConnection(String filePath) {
        InputStream input;
        try {
            input = new FileInputStream(filePath);
            properties.load(input);
        Class.forName(properties.getProperty("jdbc.driver"));
        String url = properties.getProperty("jdbc.url");
        String login = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");
            connection = DriverManager.getConnection(url, login, password);
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SqlRuParse sqlRuParse = new SqlRuParse();
        AlertRabbit alertRabbit = new AlertRabbit(
                new Properties(),
                "src/main/resources/rabbit.properties");
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connection", connection);
            data.put("parse", sqlRuParse);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(5)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(5000);
            scheduler.shutdown();

        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {

        private final StoreRabbitDemo storeRabbitDemo;

        public Rabbit() {
           storeRabbitDemo = new StoreRabbitDemo(connection);
        }

        @Override
        public void execute(JobExecutionContext context) {
            storeRabbitDemo.add(new Date(System.currentTimeMillis()));
            SqlRuParse sqlRuParse = (SqlRuParse) context.getJobDetail().getJobDataMap().get("parse");
            sqlRuParse.siteParse(PAGE);
        }
    }
}
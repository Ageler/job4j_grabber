package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {

    private Properties properties;

    private PreparedStatement statement;

    private static Connection connection;

    public AlertRabbit(Properties properties, String filepath) {
        this.properties = properties;
        initConnection(filepath);
    }

    public void initConnection(String filePath) {
        try (InputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            Class.forName(properties.getProperty("jdbc.driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = properties.getProperty("jdbc.url");
        String login = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");
        try {
            connection = DriverManager.getConnection(url, login, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {

        AlertRabbit alertRabbit = new AlertRabbit(
                new Properties(),
                "src/main/resources/rabbit.properties");
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connection", connection);
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
        public void execute(JobExecutionContext context) throws JobExecutionException {
            Connection connection = (Connection) context.getJobDetail().getJobDataMap().get("connection");
            storeRabbitDemo.add(new Date(System.currentTimeMillis()));
            System.out.println("Rabbit runs here ...");
        }
    }
}
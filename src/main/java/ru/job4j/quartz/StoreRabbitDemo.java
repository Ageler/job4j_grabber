package ru.job4j.quartz;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class StoreRabbitDemo {

    Connection cn;

    public StoreRabbitDemo(Connection connection) {
        this.cn = connection;
    }

    public boolean add(Date date) {

        try (PreparedStatement st = cn.prepareStatement("INSERT INTO rabbit(created_date) VALUES (?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ResultSet rs = st.getGeneratedKeys();
            st.setTimestamp(1, new Timestamp(date.getTime()));
            st.executeUpdate();
            if (rs != null && rs.next()) {
                return false;
            }
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
        return true;
    }
}






package ru.job4j.quartz;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.Date;

@RequiredArgsConstructor
public class StoreRabbitDemo {

    private final Connection cn;

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






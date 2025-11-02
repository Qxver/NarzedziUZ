package org.store.narzedziuz;
import java.sql.*;

public class CreateDatabase {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/?user=root&password=root123&serverTimezone=UTC";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE DATABASE IF NOT EXISTS NarzedziUZDB";
            stmt.executeUpdate(sql);

            System.out.println("Baza danych utworzona lub już istnieje.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


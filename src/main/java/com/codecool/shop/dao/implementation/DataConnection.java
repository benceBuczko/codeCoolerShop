package com.codecool.shop.dao.implementation;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DataConnection {

    private static Properties properties = new Properties();
    private static String host;
    private static String databaseName;
    private static String DB_URL;
    private static String USER;
    private static String PASSWORD;

    private static DataConnection instance = null;

    private DataConnection() {
        // load a properties file
        try {
            InputStream input = new FileInputStream("src/main/resources/sql/connection.properties");
            properties.load(input);
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setup();
    }

    public static DataConnection getInstance() {
        if (instance == null) {
            instance = new DataConnection();
        }
        return instance;
    }

    private static void setup() {
        host = properties.getProperty("url");
        databaseName = properties.getProperty("database");
        DB_URL = "jdbc:postgresql://" + host + "/" + databaseName;
        USER = properties.getProperty("user");
        PASSWORD = properties.getProperty("password");

    }

    public Connection getConnection() throws SQLException {
           return DriverManager.getConnection(
                    DB_URL,
                    USER,
                    PASSWORD);
    }
}

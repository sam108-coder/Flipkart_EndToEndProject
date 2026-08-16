package com.sam.database;

import com.sam.utilities.ConfigReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(

                ConfigReader.get("db.url"),

                ConfigReader.get("db.username"),

                ConfigReader.get("db.password")
        );
    }
}

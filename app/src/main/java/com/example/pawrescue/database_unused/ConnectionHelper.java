package com.example.pawrescue.database_unused;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {

    private static final String DB_URL = "jdbc:postgresql://192.168.131.138:5432/PawRescue-DB1";
    //private static final String DB_URL = "jdbc:sqlserver://192.168.131.138:1433/PawRescue-DB1";
    //private static final String USER = "DESKTOP-G26VU43";
    private static final String USER = "postgres";
    private static final String PASS = "12345";

    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}

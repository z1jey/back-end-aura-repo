package org.example.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public final class DbConnection {
    private static final String URL ="db.url";
    private static final String USERNAME = "db.username";
    private static final String  PASSWORD = "db.password";

    public DbConnection(){}

    public static Connection getConnection() throws SQLException {
        PropertyLoader config = PropertyLoader.getInstance();
        String url = config.getProperty(URL);
        String username = config.getProperty(USERNAME);
        String password = config.getProperty(PASSWORD);

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection established successfully!");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}

package gucant.inv.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLiteConnection {
    private static SQLiteConnection instance; // Singleton instance
    private Connection connection;
    private final String url = "jdbc:sqlite:src/main/resources/gucant/inv/database/database.db";
    private static final Logger LOGGER = Logger.getLogger(SQLiteConnection.class.getName());

    // Private constructor to prevent instantiation
    private SQLiteConnection() {
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error connecting to SQLite database: " + e.getMessage(), e);
        }
    }

    // Public method to get the Singleton instance
    public static SQLiteConnection getInstance() {
        if (instance == null) {
            synchronized (SQLiteConnection.class) {
                if (instance == null) { // Double-checked locking for thread safety
                    instance = new SQLiteConnection();
                }
            }
        }
        return instance;
    }

    // Method to get the Connection object
    public Connection getConnection() {
        return connection;
    }

    // Close the connection when the application stops
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing SQLite connection: " + e.getMessage(), e);
            }
        }
    }
}
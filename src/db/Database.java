package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles the single database connection and makes sure
 * the schema (tables) exist before the app starts.
 */
public class Database {

    private static final String DB_URL = System.getProperty("db.url", "jdbc:sqlite:hospital.db");
    private static Connection connection;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found on classpath: " + e.getMessage());
        }
    }

    // Get the shared connection (creates it once, reuses it after)
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
            connection.createStatement().execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }

    // Schema is embedded directly here (not read from an external file) so it
    // works no matter what working directory the program is launched from.
    private static final String SCHEMA_SQL =
            "CREATE TABLE IF NOT EXISTS doctors (" +
            "    id              INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    name            TEXT NOT NULL," +
            "    specialization  TEXT NOT NULL," +
            "    phone           TEXT" +
            ");" +
            "CREATE TABLE IF NOT EXISTS patients (" +
            "    id              INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    name            TEXT NOT NULL," +
            "    age             INTEGER NOT NULL," +
            "    gender          TEXT," +
            "    phone           TEXT," +
            "    address         TEXT," +
            "    disease         TEXT," +
            "    admit_date      TEXT" +
            ");" +
            "CREATE TABLE IF NOT EXISTS appointments (" +
            "    id                  INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    patient_id          INTEGER NOT NULL," +
            "    doctor_id           INTEGER NOT NULL," +
            "    appointment_date    TEXT NOT NULL," +
            "    status              TEXT DEFAULT 'Scheduled'," +
            "    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE," +
            "    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE" +
            ");" +
            "CREATE TABLE IF NOT EXISTS bills (" +
            "    id              INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    patient_id      INTEGER NOT NULL," +
            "    description     TEXT," +
            "    amount          REAL NOT NULL," +
            "    paid            INTEGER DEFAULT 0," +
            "    bill_date       TEXT," +
            "    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE" +
            ");";

    // Runs the schema once at startup so tables always exist
    public static void initializeSchema() {
        try (Statement stmt = getConnection().createStatement()) {
            for (String statement : SCHEMA_SQL.split(";")) {
                if (!statement.trim().isEmpty()) {
                    stmt.execute(statement);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize schema: " + e.getMessage());
        }
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}

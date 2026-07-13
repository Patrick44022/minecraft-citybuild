package de.citybuild.database;

import org.bukkit.plugin.java.JavaPlugin;
import java.sql.*;
import java.io.File;

public class DatabaseManager {
    private final JavaPlugin plugin;
    private Connection connection;
    private final String dbPath;

    public DatabaseManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dbPath = plugin.getDataFolder() + File.separator + "citybuild.db";
    }

    public void initialize() {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            
            createTables();
            plugin.getLogger().info("Datenbank initialisiert!");
        } catch (Exception e) {
            plugin.getLogger().severe("Fehler bei Datenbankinitialisierung: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Plots Tabelle
            stmt.execute("CREATE TABLE IF NOT EXISTS plots (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "uuid TEXT NOT NULL," +
                    "x1 INTEGER NOT NULL," +
                    "y1 INTEGER NOT NULL," +
                    "z1 INTEGER NOT NULL," +
                    "x2 INTEGER NOT NULL," +
                    "y2 INTEGER NOT NULL," +
                    "z2 INTEGER NOT NULL," +
                    "world TEXT NOT NULL," +
                    "owner TEXT NOT NULL," +
                    "plot_type TEXT NOT NULL," +
                    "price DOUBLE DEFAULT 0," +
                    "for_sale BOOLEAN DEFAULT 0," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");

            // Zonen Tabelle
            stmt.execute("CREATE TABLE IF NOT EXISTS zones (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "zone_id TEXT NOT NULL UNIQUE," +
                    "name TEXT NOT NULL," +
                    "zone_type TEXT NOT NULL," +
                    "x1 INTEGER NOT NULL," +
                    "y1 INTEGER NOT NULL," +
                    "z1 INTEGER NOT NULL," +
                    "x2 INTEGER NOT NULL," +
                    "y2 INTEGER NOT NULL," +
                    "z2 INTEGER NOT NULL," +
                    "world TEXT NOT NULL," +
                    "level INTEGER DEFAULT 1," +
                    "development DOUBLE DEFAULT 0," +
                    "tax_rate DOUBLE DEFAULT 0.05," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");

            // Spieler-Wirtschaft Tabelle
            stmt.execute("CREATE TABLE IF NOT EXISTS player_economy (" +
                    "uuid TEXT PRIMARY KEY," +
                    "name TEXT NOT NULL," +
                    "balance DOUBLE DEFAULT 10000," +
                    "plots_owned INTEGER DEFAULT 0," +
                    "business_level INTEGER DEFAULT 0," +
                    "total_income DOUBLE DEFAULT 0," +
                    "total_spent DOUBLE DEFAULT 0," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");

            // Transaktionen Tabelle
            stmt.execute("CREATE TABLE IF NOT EXISTS transactions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "uuid TEXT NOT NULL," +
                    "amount DOUBLE NOT NULL," +
                    "reason TEXT NOT NULL," +
                    "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");

            // Gebäude Tabelle
            stmt.execute("CREATE TABLE IF NOT EXISTS buildings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "uuid TEXT NOT NULL," +
                    "plot_id INTEGER NOT NULL," +
                    "building_type TEXT NOT NULL," +
                    "level INTEGER DEFAULT 1," +
                    "condition INTEGER DEFAULT 100," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY(plot_id) REFERENCES plots(id)" +
                    ")");

            plugin.getLogger().info("Tabellen erfolgreich erstellt/verifiziert!");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void shutdown() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                plugin.getLogger().info("Datenbankverbindung geschlossen.");
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Schließen der Datenbank: " + e.getMessage());
        }
    }
}
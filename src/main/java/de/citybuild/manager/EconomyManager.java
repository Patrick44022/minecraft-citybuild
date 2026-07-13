package de.citybuild.manager;

import de.citybuild.CityBuildPlugin;
import de.citybuild.model.PlayerEconomy;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.*;

public class EconomyManager {
    private final JavaPlugin plugin;
    private final Map<UUID, PlayerEconomy> playerEconomyCache = new HashMap<>();
    private static final double STARTING_BALANCE = 10000.0;

    public EconomyManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public PlayerEconomy getOrCreatePlayer(UUID uuid, String playerName) {
        if (playerEconomyCache.containsKey(uuid)) {
            return playerEconomyCache.get(uuid);
        }
        
        String sql = "SELECT * FROM player_economy WHERE uuid = ?";
        try (Connection conn = CityBuildPlugin.getInstance().getDatabaseManager().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, uuid.toString());
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                PlayerEconomy pe = new PlayerEconomy(
                    UUID.fromString(rs.getString("uuid")),
                    rs.getString("name"),
                    rs.getDouble("balance")
                );
                playerEconomyCache.put(uuid, pe);
                return pe;
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Laden der Spieler-Wirtschaft: " + e.getMessage());
        }
        
        // Neuen Spieler erstellen
        return createNewPlayer(uuid, playerName);
    }

    private PlayerEconomy createNewPlayer(UUID uuid, String playerName) {
        String sql = "INSERT INTO player_economy (uuid, name, balance) VALUES (?, ?, ?)";
        
        try (Connection conn = CityBuildPlugin.getInstance().getDatabaseManager().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, playerName);
            pstmt.setDouble(3, STARTING_BALANCE);
            
            pstmt.executeUpdate();
            
            PlayerEconomy pe = new PlayerEconomy(uuid, playerName, STARTING_BALANCE);
            playerEconomyCache.put(uuid, pe);
            return pe;
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Erstellen eines neuen Spielers: " + e.getMessage());
        }
        return null;
    }

    public void addBalance(UUID uuid, double amount, String reason) {
        PlayerEconomy pe = playerEconomyCache.get(uuid);
        if (pe != null) {
            pe.setBalance(pe.getBalance() + amount);
            pe.setTotalIncome(pe.getTotalIncome() + amount);
            updatePlayerBalance(pe);
            logTransaction(uuid, amount, reason);
        }
    }

    public void removeBalance(UUID uuid, double amount, String reason) {
        PlayerEconomy pe = playerEconomyCache.get(uuid);
        if (pe != null && pe.getBalance() >= amount) {
            pe.setBalance(pe.getBalance() - amount);
            pe.setTotalSpent(pe.getTotalSpent() + amount);
            updatePlayerBalance(pe);
            logTransaction(uuid, -amount, reason);
        }
    }

    public double getBalance(UUID uuid) {
        PlayerEconomy pe = playerEconomyCache.get(uuid);
        return pe != null ? pe.getBalance() : 0;
    }

    public boolean hasBalance(UUID uuid, double amount) {
        return getBalance(uuid) >= amount;
    }

    private void updatePlayerBalance(PlayerEconomy pe) {
        String sql = "UPDATE player_economy SET balance = ?, total_income = ?, total_spent = ? WHERE uuid = ?";
        
        try (Connection conn = CityBuildPlugin.getInstance().getDatabaseManager().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, pe.getBalance());
            pstmt.setDouble(2, pe.getTotalIncome());
            pstmt.setDouble(3, pe.getTotalSpent());
            pstmt.setString(4, pe.getUuid().toString());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Aktualisieren des Spieler-Guthabens: " + e.getMessage());
        }
    }

    private void logTransaction(UUID uuid, double amount, String reason) {
        String sql = "INSERT INTO transactions (uuid, amount, reason) VALUES (?, ?, ?)";
        
        try (Connection conn = CityBuildPlugin.getInstance().getDatabaseManager().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, uuid.toString());
            pstmt.setDouble(2, amount);
            pstmt.setString(3, reason);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Protokollieren der Transaktion: " + e.getMessage());
        }
    }
}
package de.citybuild.manager;

import de.citybuild.CityBuildPlugin;
import de.citybuild.model.Zone;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.*;

public class ZoneManager {
    private final JavaPlugin plugin;
    private final Map<String, Zone> zoneCache = new HashMap<>();

    public ZoneManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadAllZones();
    }

    public void createZone(String name, String zoneType, Location pos1, Location pos2) {
        String zoneId = UUID.randomUUID().toString();
        String sql = "INSERT INTO zones (zone_id, name, zone_type, x1, y1, z1, x2, y2, z2, world) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = CityBuildPlugin.getInstance().getDatabaseManager().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, zoneId);
            pstmt.setString(2, name);
            pstmt.setString(3, zoneType);
            pstmt.setInt(4, pos1.getBlockX());
            pstmt.setInt(5, pos1.getBlockY());
            pstmt.setInt(6, pos1.getBlockZ());
            pstmt.setInt(7, pos2.getBlockX());
            pstmt.setInt(8, pos2.getBlockY());
            pstmt.setInt(9, pos2.getBlockZ());
            pstmt.setString(10, pos1.getWorld().getName());
            
            pstmt.executeUpdate();
            
            Zone zone = new Zone(zoneId, name, zoneType, pos1, pos2);
            zoneCache.put(zoneId, zone);
            
            plugin.getLogger().info("Zone " + name + " erstellt!");
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Erstellen der Zone: " + e.getMessage());
        }
    }

    public Zone getZone(String zoneId) {
        return zoneCache.get(zoneId);
    }

    public Zone getZoneAtLocation(Location location) {
        for (Zone zone : zoneCache.values()) {
            if (zone.isInZone(location)) {
                return zone;
            }
        }
        return null;
    }

    public List<Zone> getZonesByType(String zoneType) {
        List<Zone> zones = new ArrayList<>();
        for (Zone zone : zoneCache.values()) {
            if (zone.getZoneType().equals(zoneType)) {
                zones.add(zone);
            }
        }
        return zones;
    }

    public void upgradeZone(String zoneId, double developmentAmount) {
        Zone zone = zoneCache.get(zoneId);
        if (zone != null) {
            zone.upgradeDevelopment(developmentAmount);
            updateZoneInDatabase(zone);
        }
    }

    private void updateZoneInDatabase(Zone zone) {
        String sql = "UPDATE zones SET level = ?, development = ? WHERE zone_id = ?";
        
        try (Connection conn = CityBuildPlugin.getInstance().getDatabaseManager().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, zone.getLevel());
            pstmt.setDouble(2, zone.getDevelopment());
            pstmt.setString(3, zone.getZoneId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Aktualisieren der Zone: " + e.getMessage());
        }
    }

    private void loadAllZones() {
        String sql = "SELECT * FROM zones";
        
        try (Connection conn = CityBuildPlugin.getInstance().getDatabaseManager().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String zoneId = rs.getString("zone_id");
                String name = rs.getString("name");
                String zoneType = rs.getString("zone_type");
                int x1 = rs.getInt("x1");
                int y1 = rs.getInt("y1");
                int z1 = rs.getInt("z1");
                int x2 = rs.getInt("x2");
                int y2 = rs.getInt("y2");
                int z2 = rs.getInt("z2");
                String world = rs.getString("world");
                
                Location pos1 = new Location(plugin.getServer().getWorld(world), x1, y1, z1);
                Location pos2 = new Location(plugin.getServer().getWorld(world), x2, y2, z2);
                
                Zone zone = new Zone(zoneId, name, zoneType, pos1, pos2);
                zone.setLevel(rs.getInt("level"));
                zone.setDevelopment(rs.getDouble("development"));
                zone.setTaxRate(rs.getDouble("tax_rate"));
                
                zoneCache.put(zoneId, zone);
            }
            plugin.getLogger().info("Zonen geladen: " + zoneCache.size());
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Laden der Zonen: " + e.getMessage());
        }
    }
}
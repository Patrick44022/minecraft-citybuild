package de.citybuild.manager;

import de.citybuild.CityBuildPlugin;
import de.citybuild.model.Plot;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.*;

public class PlotManager {
    private final JavaPlugin plugin;
    private final Map<Integer, Plot> plotCache = new HashMap<>();

    public PlotManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadAllPlots();
    }

    public void createPlot(UUID owner, Location pos1, Location pos2, String plotType) {
        String sql = "INSERT INTO plots (uuid, x1, y1, z1, x2, y2, z2, world, owner, plot_type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = CityBuildPlugin.getInstance().getDatabaseManager().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setInt(2, pos1.getBlockX());
            pstmt.setInt(3, pos1.getBlockY());
            pstmt.setInt(4, pos1.getBlockZ());
            pstmt.setInt(5, pos2.getBlockX());
            pstmt.setInt(6, pos2.getBlockY());
            pstmt.setInt(7, pos2.getBlockZ());
            pstmt.setString(8, pos1.getWorld().getName());
            pstmt.setString(9, owner.toString());
            pstmt.setString(10, plotType);
            
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int plotId = rs.getInt(1);
                    Plot plot = new Plot(plotId, owner, pos1, pos2, plotType);
                    plotCache.put(plotId, plot);
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Erstellen des Plots: " + e.getMessage());
        }
    }

    public Plot getPlot(int plotId) {
        return plotCache.get(plotId);
    }

    public Plot getPlotAtLocation(Location location) {
        for (Plot plot : plotCache.values()) {
            if (plot.isInPlot(location)) {
                return plot;
            }
        }
        return null;
    }

    public List<Plot> getPlayerPlots(UUID owner) {
        List<Plot> playerPlots = new ArrayList<>();
        for (Plot plot : plotCache.values()) {
            if (plot.getOwner().equals(owner)) {
                playerPlots.add(plot);
            }
        }
        return playerPlots;
    }

    public void sellPlot(int plotId, double price) {
        Plot plot = plotCache.get(plotId);
        if (plot != null) {
            plot.setPrice(price);
            plot.setForSale(true);
            updatePlotInDatabase(plot);
        }
    }

    public void buyPlot(int plotId, UUID newOwner) {
        Plot plot = plotCache.get(plotId);
        if (plot != null && plot.isForSale()) {
            plot.setOwner(newOwner);
            plot.setForSale(false);
            updatePlotInDatabase(plot);
        }
    }

    private void updatePlotInDatabase(Plot plot) {
        String sql = "UPDATE plots SET owner = ?, price = ?, for_sale = ?, plot_type = ? WHERE id = ?";
        
        try (Connection conn = CityBuildPlugin.getInstance().getDatabaseManager().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, plot.getOwner().toString());
            pstmt.setDouble(2, plot.getPrice());
            pstmt.setBoolean(3, plot.isForSale());
            pstmt.setString(4, plot.getPlotType());
            pstmt.setInt(5, plot.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Aktualisieren des Plots: " + e.getMessage());
        }
    }

    private void loadAllPlots() {
        String sql = "SELECT * FROM plots";
        
        try (Connection conn = CityBuildPlugin.getInstance().getDatabaseManager().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                UUID owner = UUID.fromString(rs.getString("owner"));
                int x1 = rs.getInt("x1");
                int y1 = rs.getInt("y1");
                int z1 = rs.getInt("z1");
                int x2 = rs.getInt("x2");
                int y2 = rs.getInt("y2");
                int z2 = rs.getInt("z2");
                String world = rs.getString("world");
                String plotType = rs.getString("plot_type");
                
                Location pos1 = new Location(plugin.getServer().getWorld(world), x1, y1, z1);
                Location pos2 = new Location(plugin.getServer().getWorld(world), x2, y2, z2);
                
                Plot plot = new Plot(id, owner, pos1, pos2, plotType);
                plotCache.put(id, plot);
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Laden der Plots: " + e.getMessage());
        }
    }
}
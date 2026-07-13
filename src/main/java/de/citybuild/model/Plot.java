package de.citybuild.model;

import org.bukkit.Location;
import org.bukkit.World;
import java.util.UUID;

public class Plot {
    private int id;
    private UUID owner;
    private Location pos1;
    private Location pos2;
    private String plotType;
    private double price;
    private boolean forSale;
    private String developmentStatus;

    public Plot(int id, UUID owner, Location pos1, Location pos2, String plotType) {
        this.id = id;
        this.owner = owner;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.plotType = plotType;
        this.price = 0;
        this.forSale = false;
        this.developmentStatus = "EMPTY";
    }

    // Getters & Setters
    public int getId() { return id; }
    public UUID getOwner() { return owner; }
    public void setOwner(UUID owner) { this.owner = owner; }
    
    public Location getPos1() { return pos1; }
    public Location getPos2() { return pos2; }
    
    public String getPlotType() { return plotType; }
    public void setPlotType(String plotType) { this.plotType = plotType; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public boolean isForSale() { return forSale; }
    public void setForSale(boolean forSale) { this.forSale = forSale; }
    
    public String getDevelopmentStatus() { return developmentStatus; }
    public void setDevelopmentStatus(String status) { this.developmentStatus = status; }
    
    public boolean isInPlot(Location location) {
        if (!location.getWorld().equals(pos1.getWorld())) return false;
        
        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        
        return location.getBlockX() >= minX && location.getBlockX() <= maxX &&
               location.getBlockZ() >= minZ && location.getBlockZ() <= maxZ;
    }
    
    public double getArea() {
        int width = Math.abs(pos2.getBlockX() - pos1.getBlockX()) + 1;
        int length = Math.abs(pos2.getBlockZ() - pos1.getBlockZ()) + 1;
        return width * length;
    }
}
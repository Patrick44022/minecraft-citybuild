package de.citybuild.model;

import org.bukkit.Location;

public class Zone {
    private String zoneId;
    private String name;
    private String zoneType; // RESIDENTIAL, COMMERCIAL, INDUSTRIAL, PARK, PUBLIC
    private Location pos1;
    private Location pos2;
    private int level;
    private double development;
    private double taxRate;

    public Zone(String zoneId, String name, String zoneType, Location pos1, Location pos2) {
        this.zoneId = zoneId;
        this.name = name;
        this.zoneType = zoneType;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.level = 1;
        this.development = 0;
        this.taxRate = 0.05;
    }

    // Getters & Setters
    public String getZoneId() { return zoneId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getZoneType() { return zoneType; }
    public Location getPos1() { return pos1; }
    public Location getPos2() { return pos2; }
    
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    
    public double getDevelopment() { return development; }
    public void setDevelopment(double development) { this.development = development; }
    
    public double getTaxRate() { return taxRate; }
    public void setTaxRate(double taxRate) { this.taxRate = taxRate; }
    
    public boolean isInZone(Location location) {
        if (!location.getWorld().equals(pos1.getWorld())) return false;
        
        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        
        return location.getBlockX() >= minX && location.getBlockX() <= maxX &&
               location.getBlockZ() >= minZ && location.getBlockZ() <= maxZ;
    }
    
    public void upgradeDevelopment(double amount) {
        this.development += amount;
        if (development >= 100 * level) {
            level++;
            development = 0;
        }
    }
}
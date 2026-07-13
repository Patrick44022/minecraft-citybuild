package de.citybuild.model;

import java.util.UUID;

public class PlayerEconomy {
    private UUID uuid;
    private String name;
    private double balance;
    private int plotsOwned;
    private int businessLevel;
    private double totalIncome;
    private double totalSpent;

    public PlayerEconomy(UUID uuid, String name, double balance) {
        this.uuid = uuid;
        this.name = name;
        this.balance = balance;
        this.plotsOwned = 0;
        this.businessLevel = 0;
        this.totalIncome = 0;
        this.totalSpent = 0;
    }

    // Getters & Setters
    public UUID getUuid() { return uuid; }
    public String getName() { return name; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    
    public int getPlotsOwned() { return plotsOwned; }
    public void setPlotsOwned(int count) { this.plotsOwned = count; }
    
    public int getBusinessLevel() { return businessLevel; }
    public void setBusinessLevel(int level) { this.businessLevel = level; }
    
    public double getTotalIncome() { return totalIncome; }
    public void setTotalIncome(double income) { this.totalIncome = income; }
    
    public double getTotalSpent() { return totalSpent; }
    public void setTotalSpent(double spent) { this.totalSpent = spent; }
}
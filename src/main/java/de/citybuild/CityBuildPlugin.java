package de.citybuild;

import de.citybuild.database.DatabaseManager;
import de.citybuild.listener.PlayerListener;
import de.citybuild.listener.BlockListener;
import de.citybuild.commands.CityCommand;
import de.citybuild.commands.PlotCommand;
import de.citybuild.commands.ZoneCommand;
import de.citybuild.commands.EconomyCommand;
import de.citybuild.manager.PlotManager;
import de.citybuild.manager.ZoneManager;
import de.citybuild.manager.EconomyManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CityBuildPlugin extends JavaPlugin {

    private static CityBuildPlugin instance;
    private DatabaseManager databaseManager;
    private PlotManager plotManager;
    private ZoneManager zoneManager;
    private EconomyManager economyManager;

    @Override
    public void onEnable() {
        instance = this;
        
        getLogger().info("========================================");
        getLogger().info("CityBuild Plugin wird geladen...");
        getLogger().info("========================================");

        // Datenbank initialisieren
        databaseManager = new DatabaseManager(this);
        databaseManager.initialize();
        
        // Manager initialisieren
        plotManager = new PlotManager(this);
        zoneManager = new ZoneManager(this);
        economyManager = new EconomyManager(this);
        
        // Listener registrieren
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        
        // Commands registrieren
        getCommand("city").setExecutor(new CityCommand(this));
        getCommand("plot").setExecutor(new PlotCommand(this));
        getCommand("zone").setExecutor(new ZoneCommand(this));
        getCommand("cityecon").setExecutor(new EconomyCommand(this));
        
        getLogger().info("CityBuild Plugin erfolgreich geladen!");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.shutdown();
        }
        getLogger().info("CityBuild Plugin deaktiviert.");
    }

    public static CityBuildPlugin getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public PlotManager getPlotManager() {
        return plotManager;
    }

    public ZoneManager getZoneManager() {
        return zoneManager;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }
}
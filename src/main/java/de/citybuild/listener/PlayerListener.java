package de.citybuild.listener;

import de.citybuild.CityBuildPlugin;
import de.citybuild.model.PlayerEconomy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final CityBuildPlugin plugin;

    public PlayerListener(CityBuildPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Spieler in der Wirtschaft registrieren
        PlayerEconomy pe = plugin.getEconomyManager().getOrCreatePlayer(
            event.getPlayer().getUniqueId(),
            event.getPlayer().getName()
        );
        
        event.getPlayer().sendMessage("§6Willkommen im CityBuild System!");
        event.getPlayer().sendMessage("§7Dein aktuelles Guthaben: §a$" + pe.getBalance());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Spielerdaten speichern
        plugin.getLogger().info(event.getPlayer().getName() + " hat den Server verlassen.");
    }
}
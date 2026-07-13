package de.citybuild.listener;

import de.citybuild.CityBuildPlugin;
import de.citybuild.model.Plot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {
    private final CityBuildPlugin plugin;

    public BlockListener(CityBuildPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Plot plot = plugin.getPlotManager().getPlotAtLocation(event.getBlock().getLocation());
        
        if (plot != null && !plot.getOwner().equals(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cDu darfst auf diesem Grundstück nichts abbauen!");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Plot plot = plugin.getPlotManager().getPlotAtLocation(event.getBlock().getLocation());
        
        if (plot != null && !plot.getOwner().equals(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cDu darfst auf diesem Grundstück nichts bauen!");
        }
    }
}
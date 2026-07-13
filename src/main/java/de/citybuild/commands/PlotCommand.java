package de.citybuild.commands;

import de.citybuild.CityBuildPlugin;
import de.citybuild.model.Plot;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlotCommand implements CommandExecutor {
    private final CityBuildPlugin plugin;
    private final java.util.Map<java.util.UUID, Location> selectionStart = new java.util.HashMap<>();
    private final java.util.Map<java.util.UUID, Location> selectionEnd = new java.util.HashMap<>();

    public PlotCommand(CityBuildPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieses Kommando kann nur von Spielern verwendet werden!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§cVerwendung: /plot <create|info|sell|info>");
            return true;
        }

        String subcommand = args[0].toLowerCase();
        switch (subcommand) {
            case "create":
                if (args.length < 2) {
                    player.sendMessage("§cVerwendung: /plot create <type>");
                    return true;
                }
                createPlot(player, args[1]);
                break;
            case "info":
                showPlotInfo(player);
                break;
            case "sell":
                if (args.length < 2) {
                    player.sendMessage("§cVerwendung: /plot sell <price>");
                    return true;
                }
                sellPlot(player, args[1]);
                break;
            case "pos1":
                setPosOne(player);
                break;
            case "pos2":
                setPosTwo(player);
                break;
            default:
                player.sendMessage("§cUnbekanntes Kommando!");
        }

        return true;
    }

    private void createPlot(Player player, String plotType) {
        Location pos1 = selectionStart.get(player.getUniqueId());
        Location pos2 = selectionEnd.get(player.getUniqueId());

        if (pos1 == null || pos2 == null) {
            player.sendMessage("§cBitte setze zuerst pos1 und pos2 mit /plot pos1 und /plot pos2!");
            return;
        }

        plugin.getPlotManager().createPlot(player.getUniqueId(), pos1, pos2, plotType);
        player.sendMessage("§aGrundstück erfolgreich erstellt! Typ: " + plotType);
        
        selectionStart.remove(player.getUniqueId());
        selectionEnd.remove(player.getUniqueId());
    }

    private void showPlotInfo(Player player) {
        Plot plot = plugin.getPlotManager().getPlotAtLocation(player.getLocation());
        
        if (plot == null) {
            player.sendMessage("§cDu stehst auf keinem Grundstück!");
            return;
        }

        player.sendMessage("\n§6=== Grundstücks-Info ===");
        player.sendMessage("§eTyp: §a" + plot.getPlotType());
        player.sendMessage("§eGröße: §a" + plot.getArea() + " Blöcke");
        player.sendMessage("§eStatus: §a" + plot.getDevelopmentStatus());
        
        if (plot.isForSale()) {
            player.sendMessage("§ePreis: §a$" + plot.getPrice());
        }
        player.sendMessage("\n ");
    }

    private void sellPlot(Player player, String priceStr) {
        Plot plot = plugin.getPlotManager().getPlotAtLocation(player.getLocation());
        
        if (plot == null) {
            player.sendMessage("§cDu stehst auf keinem Grundstück!");
            return;
        }

        if (!plot.getOwner().equals(player.getUniqueId())) {
            player.sendMessage("§cDieses Grundstück gehört dir nicht!");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            plugin.getPlotManager().sellPlot(plot.getId(), price);
            player.sendMessage("§aGrundstück zum Verkauf angeboten! Preis: $" + price);
        } catch (NumberFormatException e) {
            player.sendMessage("§cUngültiger Preis!");
        }
    }

    private void setPosOne(Player player) {
        selectionStart.put(player.getUniqueId(), player.getLocation());
        player.sendMessage("§aPos1 gesetzt!");
    }

    private void setPosTwo(Player player) {
        selectionEnd.put(player.getUniqueId(), player.getLocation());
        player.sendMessage("§aPos2 gesetzt!");
    }
}
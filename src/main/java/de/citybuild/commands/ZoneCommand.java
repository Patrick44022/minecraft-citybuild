package de.citybuild.commands;

import de.citybuild.CityBuildPlugin;
import de.citybuild.model.Zone;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ZoneCommand implements CommandExecutor {
    private final CityBuildPlugin plugin;
    private final java.util.Map<java.util.UUID, Location> selectionStart = new java.util.HashMap<>();
    private final java.util.Map<java.util.UUID, Location> selectionEnd = new java.util.HashMap<>();

    public ZoneCommand(CityBuildPlugin plugin) {
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
            player.sendMessage("§cVerwendung: /zone <create|info|upgrade>");
            return true;
        }

        String subcommand = args[0].toLowerCase();
        switch (subcommand) {
            case "create":
                if (args.length < 3) {
                    player.sendMessage("§cVerwendung: /zone create <name> <type>");
                    player.sendMessage("§7Typen: RESIDENTIAL, COMMERCIAL, INDUSTRIAL, PARK, PUBLIC");
                    return true;
                }
                createZone(player, args[1], args[2]);
                break;
            case "info":
                showZoneInfo(player);
                break;
            case "upgrade":
                upgradeZone(player);
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

    private void createZone(Player player, String name, String zoneType) {
        Location pos1 = selectionStart.get(player.getUniqueId());
        Location pos2 = selectionEnd.get(player.getUniqueId());

        if (pos1 == null || pos2 == null) {
            player.sendMessage("§cBitte setze zuerst pos1 und pos2 mit /zone pos1 und /zone pos2!");
            return;
        }

        plugin.getZoneManager().createZone(name, zoneType, pos1, pos2);
        player.sendMessage("§aZone \"" + name + "\" erfolgreich erstellt! Typ: " + zoneType);
        
        selectionStart.remove(player.getUniqueId());
        selectionEnd.remove(player.getUniqueId());
    }

    private void showZoneInfo(Player player) {
        Zone zone = plugin.getZoneManager().getZoneAtLocation(player.getLocation());
        
        if (zone == null) {
            player.sendMessage("§cDu stehst in keiner Zone!");
            return;
        }

        player.sendMessage("\n§6=== Zone Info ===");
        player.sendMessage("§eName: §a" + zone.getName());
        player.sendMessage("§eTyp: §a" + zone.getZoneType());
        player.sendMessage("§eLevel: §a" + zone.getLevel());
        player.sendMessage("§eEntwicklung: §a" + String.format("%.1f%%", zone.getDevelopment()));
        player.sendMessage("§eSteuersatz: §a" + (zone.getTaxRate() * 100) + "%");
        player.sendMessage("\n ");
    }

    private void upgradeZone(Player player) {
        Zone zone = plugin.getZoneManager().getZoneAtLocation(player.getLocation());
        
        if (zone == null) {
            player.sendMessage("§cDu stehst in keiner Zone!");
            return;
        }

        plugin.getZoneManager().upgradeZone(zone.getZoneId(), 10.0);
        player.sendMessage("§aZone aktualisiert! Neue Entwicklung: " + zone.getDevelopment());
    }

    private void setPosOne(Player player) {
        selectionStart.put(player.getUniqueId(), player.getLocation());
        player.sendMessage("§aZone Pos1 gesetzt!");
    }

    private void setPosTwo(Player player) {
        selectionEnd.put(player.getUniqueId(), player.getLocation());
        player.sendMessage("§aZone Pos2 gesetzt!");
    }
}
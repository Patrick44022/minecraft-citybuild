package de.citybuild.commands;

import de.citybuild.CityBuildPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CityCommand implements CommandExecutor {
    private final CityBuildPlugin plugin;

    public CityCommand(CityBuildPlugin plugin) {
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
            showHelp(player);
            return true;
        }

        String subcommand = args[0].toLowerCase();
        switch (subcommand) {
            case "info":
                showCityInfo(player);
                break;
            case "stats":
                showStats(player);
                break;
            case "help":
                showHelp(player);
                break;
            default:
                player.sendMessage("§cUnbekanntes Kommando! Verwende /city help");
        }

        return true;
    }

    private void showHelp(Player player) {
        player.sendMessage("\n§6=== CityBuild Hilfe ===");
        player.sendMessage("§e/plot create <type> §7- Grundstück erstellen");
        player.sendMessage("§e/plot info §7- Informationen zum aktuellen Grundstück");
        player.sendMessage("§e/plot sell <price> §7- Grundstück zum Verkauf anbieten");
        player.sendMessage("§e/zone create <name> <type> §7- Zone erstellen");
        player.sendMessage("§e/zone info §7- Zone-Informationen");
        player.sendMessage("§e/cityecon balance §7- Kontostand anzeigen");
        player.sendMessage("§e/cityecon top §7- Top 10 reichste Spieler");
        player.sendMessage("§n ");
    }

    private void showCityInfo(Player player) {
        player.sendMessage("§6CityBuild System v1.0.0");
        player.sendMessage("§7Ein vollständiges Citybuild-Plugin für PaperMC");
    }

    private void showStats(Player player) {
        player.sendMessage("\n§6=== Deine Statistiken ===");
        var econ = plugin.getEconomyManager().getOrCreatePlayer(player.getUniqueId(), player.getName());
        player.sendMessage("§eGuthaben: §a$" + econ.getBalance());
        player.sendMessage("§eGrundstücke: §a" + econ.getPlotsOwned());
        player.sendMessage("§eUnternehmensstufe: §a" + econ.getBusinessLevel());
        player.sendMessage("\n ");
    }
}
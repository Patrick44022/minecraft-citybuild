package de.citybuild.commands;

import de.citybuild.CityBuildPlugin;
import de.citybuild.model.PlayerEconomy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomyCommand implements CommandExecutor {
    private final CityBuildPlugin plugin;

    public EconomyCommand(CityBuildPlugin plugin) {
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
            player.sendMessage("§cVerwendung: /cityecon <balance|top>");
            return true;
        }

        String subcommand = args[0].toLowerCase();
        switch (subcommand) {
            case "balance":
                showBalance(player);
                break;
            case "top":
                showTop(player);
                break;
            default:
                player.sendMessage("§cUnbekanntes Kommando!");
        }

        return true;
    }

    private void showBalance(Player player) {
        PlayerEconomy pe = plugin.getEconomyManager().getOrCreatePlayer(player.getUniqueId(), player.getName());
        player.sendMessage("\n§6=== Dein Guthaben ===");
        player.sendMessage("§eGuthaben: §a$" + pe.getBalance());
        player.sendMessage("§eGesamteinkommen: §a$" + pe.getTotalIncome());
        player.sendMessage("§eGesamtausgaben: §a$" + pe.getTotalSpent());
        player.sendMessage("\n ");
    }

    private void showTop(Player player) {
        player.sendMessage("\n§6=== Top 10 Reichste Spieler ===");
        player.sendMessage("§7Funktion wird noch implementiert...");
        player.sendMessage("\n ");
    }
}
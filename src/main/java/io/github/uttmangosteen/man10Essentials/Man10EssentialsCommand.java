package io.github.uttmangosteen.man10Essentials;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public final class Man10EssentialsCommand implements CommandExecutor {

    private final Main plugin;

    public Man10EssentialsCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            String @NonNull [] args
    ) {
        if (!sender.hasPermission("red.man10.admin")) {
            sender.sendMessage(Global.PREFIX + "§cあなたは権限を持っていません");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("status")) {
            sendStatus(sender);
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("toggle")) {
            toggleFeature(sender, args[1]);
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            setFeature(sender, args[1], true);
            return true;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
            Boolean value = parseBoolean(args[2]);

            if (value == null) {
                sender.sendMessage(Global.PREFIX + "§c値は true / false / on / off で指定してください");
                return true;
            }

            setFeature(sender, args[1], value);
            return true;
        }

        sendHelp(sender, label);
        return true;
    }

    private void sendStatus(CommandSender sender) {
        sender.sendMessage("§7§m----------§r " + Global.PREFIX + "§bStatus §7§m----------");

        for (Map.Entry<String, Boolean> entry : plugin.settings().getFeatureStatuses().entrySet()) {
            String status = entry.getValue() ? "§a有効" : "§c無効";
            sender.sendMessage("§e" + entry.getKey() + "§7: " + status);
        }

        sender.sendMessage("§ewhitelist§7: " + (Bukkit.hasWhitelist() ? "§aON" : "§cOFF"));
    }

    private void toggleFeature(CommandSender sender, String key) {
        if (!plugin.settings().exists(key)) {
            sender.sendMessage(Global.PREFIX + "§c存在しない設定です: " + key);
            return;
        }

        boolean enabled = plugin.settings().toggle(key);
        sender.sendMessage(Global.PREFIX + "§e" + key + "§7 を " + format(enabled) + "§7 にしました");
    }

    private void setFeature(CommandSender sender, String key, boolean enabled) {
        if (!plugin.settings().exists(key)) {
            sender.sendMessage(Global.PREFIX + "§c存在しない設定です: " + key);
            return;
        }

        plugin.settings().setEnabled(key, enabled);
        sender.sendMessage(Global.PREFIX + "§e" + key + "§7 を " + format(enabled) + "§7 にしました");
    }

    private Boolean parseBoolean(String value) {
        return switch (value.toLowerCase()) {
            case "true", "on", "enable", "enabled" -> true;
            case "false", "off", "disable", "disabled" -> false;
            default -> null;
        };
    }

    private String format(boolean enabled) {
        return enabled ? "§a有効" : "§c無効";
    }

    private void sendHelp(CommandSender sender, String label) {
        sender.sendMessage(Global.PREFIX + "§c使い方:");
        sender.sendMessage("§e/" + label + " status §7- 機能状態一覧");
        sender.sendMessage("§e/" + label + " toggle <key> §7- 機能のON/OFF切替");
        sender.sendMessage("§e/" + label + " set <key> <true|false> §7- 機能状態を指定");
    }
}
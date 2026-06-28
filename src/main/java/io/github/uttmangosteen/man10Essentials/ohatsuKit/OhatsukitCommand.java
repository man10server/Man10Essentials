package io.github.uttmangosteen.man10Essentials.ohatsuKit;

import io.github.uttmangosteen.man10Essentials.Global;
import io.github.uttmangosteen.man10Essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class OhatsukitCommand implements CommandExecutor {

    private final Main plugin;

    public OhatsukitCommand(Main plugin) {
        this.plugin = plugin;
    }

    private List<Map<String, Object>> serializeItems(ItemStack[] items) {
        List<Map<String, Object>> serialized = new ArrayList<>();

        for (ItemStack item : items) {
            serialized.add(item == null ? null : item.serialize());
        }

        return serialized;
    }

    private ItemStack[] deserializeItems(List<?> list) {
        if (list == null) {
            return null;
        }

        ItemStack[] items = new ItemStack[list.size()];

        for (int i = 0; i < list.size(); i++) {
            Object value = list.get(i);

            if (!(value instanceof Map<?, ?> map)) {
                items[i] = null;
                continue;
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> itemData = (Map<String, Object>) map;

            items[i] = ItemStack.deserialize(itemData);
        }

        return items;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            String @NonNull [] args
    ) {
        if (!sender.hasPermission("red.man10.ohatsukit")) {
            sender.sendMessage(Global.PREFIX + "§cあなたは権限を持っていません");
            return true;
        }

        if (args.length == 1) {
            return handleSingleArgument(sender, args[0]);
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            return giveKit(sender, args[1]);
        }

        sendHelp(sender, label);
        return true;
    }

    private boolean handleSingleArgument(CommandSender sender, String arg) {
        switch (arg.toLowerCase()) {
            case "on" -> {
                plugin.settings().setEnabled("ohatsukit", true);
                sender.sendMessage(Global.PREFIX + "§a初期装備を配布します");
                return true;
            }
            case "off" -> {
                plugin.settings().setEnabled("ohatsukit", false);
                sender.sendMessage(Global.PREFIX + "§c初期装備は配られません");
                return true;
            }
            case "register" -> {
                return registerKit(sender);
            }
            default -> {
                return false;
            }
        }
    }

    private boolean registerKit(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Global.PREFIX + "§cこのコマンドはプレイヤーのみ実行できます");
            return true;
        }

        PlayerInventory inventory = player.getInventory();
        plugin.getConfig().set("ohatsukit.inv", serializeItems(inventory.getContents()));
        plugin.saveConfig();

        sender.sendMessage(Global.PREFIX + "§a現在の装備と所持品を登録しました");
        return true;
    }

    private boolean giveKit(CommandSender sender, String playerName) {
        List<?> savedInventory = plugin.getConfig().getList("ohatsukit.inv");

        if (savedInventory == null) {
            sender.sendMessage(Global.PREFIX + "§ckitが登録されていません");
            return true;
        }

        Player target = Bukkit.getPlayer(playerName);

        if (target == null) {
            sender.sendMessage(Global.PREFIX + "§cプレイヤーが見つかりません");
            return true;
        }

        ItemStack[] kitItems = deserializeItems(savedInventory);

        if (kitItems == null) {
            sender.sendMessage(Global.PREFIX + "§ckitの読み込みに失敗しました");
            return true;
        }

        giveItems(target, kitItems);

        sender.sendMessage(Global.PREFIX + "§a" + target.getName() + "に初期装備を付与しました");
        return true;
    }

    private void giveItems(Player target, ItemStack[] kitItems) {
        ItemStack[] contents = target.getInventory().getContents();

        for (int i = 0; i < kitItems.length && i < contents.length; i++) {
            if (kitItems[i] != null && contents[i] == null) {
                contents[i] = kitItems[i];
                kitItems[i] = null;
            }
        }

        target.getInventory().setContents(contents);

        for (ItemStack item : kitItems) {
            if (item == null) {
                continue;
            }

            if (target.getInventory().firstEmpty() == -1) {
                target.sendMessage(Global.PREFIX + "§cインベントリに空きがなかったため一部のアイテムは消えました");
                return;
            }

            target.getInventory().addItem(item);
        }
    }

    private void sendHelp(CommandSender sender, String label) {
        sender.sendMessage(Global.PREFIX + "§c使い方:");
        sender.sendMessage("§e/" + label + " <on|off>");
        sender.sendMessage("§e/" + label + " register");
        sender.sendMessage("§e/" + label + " give <player>");
    }
}
package io.github.uttmangosteen.man10Essentials.ohatsuKit;

import io.github.uttmangosteen.man10Essentials.Global;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record OhatsukitCommand(JavaPlugin plugin) implements CommandExecutor {

    // ItemStack[] Base64 変換関数
    private List<Map<String, Object>> itemStackArrayToConfigList(ItemStack[] items) {
        List<Map<String, Object>> configList = new ArrayList<>();
        for (ItemStack item : items) {
            if (item != null) {
                configList.add(item.serialize());
            } else {
                configList.add(null);
            }
        }
        return configList;
    }


    private ItemStack[] itemStackArrayFromConfigList(List<?> configList) {
        if (configList == null) return null;

        ItemStack[] items = new ItemStack[configList.size()];
        for (int i = 0; i < configList.size(); i++) {
            if (configList.get(i) != null && configList.get(i) instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> itemData = (Map<String, Object>) configList.get(i);
                items[i] = ItemStack.deserialize(itemData);
            } else {
                items[i] = null;
            }
        }
        return items;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("red.man10.ohatsukit")) return true;
        switch (args.length) {
            case 1:
                switch (args[0]) {
                    case "on":
                        Global.enabled_give_ohatsukit = true;
                        plugin.getConfig().set("ohatsukit.mode", true);
                        plugin.saveConfig();
                        sender.sendMessage(Global.prefix + "§a初期装備を配布します§r");
                        return true;
                    case "off":
                        Global.enabled_give_ohatsukit = false;
                        plugin.getConfig().set("ohatsukit.mode", false);
                        plugin.saveConfig();
                        sender.sendMessage(Global.prefix + "§c初期装備は配られません§r");
                        return true;
                    case "register":
                        if (!(sender instanceof Player player)) {
                            sender.sendMessage(Global.prefix + "§cこのコマンドはプレイヤーのみ実行できます§r");
                            return true;
                        }
                        PlayerInventory inv = player.getInventory();
                        List<Map<String, Object>> inventoryData = itemStackArrayToConfigList(inv.getContents());
                        plugin.getConfig().set("ohatsukit.inv", inventoryData);
                        plugin.saveConfig();
                        sender.sendMessage(Global.prefix + "§a現在の装備と所持品を登録しました§r");
                        return true;
                    default:
                        return false;
                }
            case 2:
                if (!args[0].equalsIgnoreCase("give")) return false;
                List<?> savedInvData = plugin.getConfig().getList("ohatsukit.inv");
                if (savedInvData == null) {
                    sender.sendMessage(Global.prefix + "§ckitが登録されていません§r");
                    return true;
                }
                Player targetPlayer = Bukkit.getPlayer(args[1]);
                if (targetPlayer == null) {
                    sender.sendMessage(Global.prefix + "§cプレイヤーが見つかりません§r");
                    return true;
                }
                ItemStack[] inv = itemStackArrayFromConfigList(savedInvData);
                ItemStack[] targetInv = targetPlayer.getInventory().getContents();
                if (inv != null) {
                    //同じ場所が空いていたらできるだけ同じ場所へ
                    for (int i = 0; i < inv.length; i++) {
                        if (inv[i] != null && targetInv[i] == null) {
                            targetInv[i] = inv[i];
                            inv[i] = null;
                        }
                    }
                    targetPlayer.getInventory().setContents(targetInv);
                    // 残りのアイテムを空いているスロットに追加
                    for (ItemStack invItem : inv) {
                        if (invItem != null) {
                            if (targetPlayer.getInventory().firstEmpty() != -1) {
                                targetPlayer.getInventory().addItem(invItem);
                                continue;
                            }
                            targetPlayer.sendMessage(Global.prefix + "§c" + targetPlayer.getName() + "のインベントリに空きがなかったため一部のアイテムは消えました§r");
                            break;
                        }
                    }
                }
                sender.sendMessage(Global.prefix + "§a" + targetPlayer.getName() + "に初期装備を付与しました§r");
                return true;
        }
        return false;
    }
}

package io.github.uttmangosteen.man10Essentials.mhat;

import io.github.uttmangosteen.man10Essentials.Global;
import io.github.uttmangosteen.man10Essentials.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class MHatCommand implements CommandExecutor {

    private final Main plugin;

    public MHatCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String alias,
            @NotNull String[] args
    ) {
        if (!plugin.settings().isEnabled("mhat")) {
            sender.sendMessage(Global.PREFIX + "§cこのコマンドは現在無効です");
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Global.PREFIX + "§cプレイヤーのみ実行できます");
            return true;
        }

        if (!player.hasPermission("red.man10.mhat")) {
            player.sendMessage(Global.PREFIX + "§cあなたは権限を持っていません");
            return true;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();
        ItemStack helmet = player.getInventory().getHelmet();

        player.getInventory().setItemInMainHand(helmet);
        player.getInventory().setHelmet(hand);
        player.updateInventory();

        player.sendMessage(Global.PREFIX + "§aアイテムを頭にかぶりました");
        return true;
    }
}
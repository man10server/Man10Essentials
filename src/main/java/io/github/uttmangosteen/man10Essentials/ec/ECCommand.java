package io.github.uttmangosteen.man10Essentials.ec;

import io.github.uttmangosteen.man10Essentials.Global;
import io.github.uttmangosteen.man10Essentials.Main;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public final class ECCommand implements CommandExecutor {

    private final Main plugin;

    public ECCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String alias,
            @NotNull String @NonNull [] args
    ) {
        if (!plugin.settings().isEnabled("ec")) {
            sender.sendMessage(Global.PREFIX + "§cこのコマンドは現在無効です");
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Global.PREFIX + "§cプレイヤーのみ実行できます");
            return true;
        }

        if (!sender.hasPermission("red.man10.ec")) {
            sender.sendMessage(Global.PREFIX + "§cあなたは権限を持っていません");
            return true;
        }

        player.openInventory(player.getEnderChest());
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1, 1);
        return true;
    }
}
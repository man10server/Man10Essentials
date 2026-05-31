package io.github.uttmangosteen.man10Essentials.ec;

import io.github.uttmangosteen.man10Essentials.Global;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ECCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String alias, String[] args) {
        if (!(sender instanceof Player p)) return true;
        if (!sender.hasPermission("red.man10.ec")) {
            sender.sendMessage(Global.prefix + "§4あなたは権限を持っていません§r");
            return true;
        }
        p.openInventory(p.getEnderChest());
        p.playSound(p.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1, 1);
        return true;
    }
}

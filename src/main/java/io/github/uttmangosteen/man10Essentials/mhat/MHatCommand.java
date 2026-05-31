package io.github.uttmangosteen.man10Essentials.mhat;

import io.github.uttmangosteen.man10Essentials.Global;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MHatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String alias, String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage(Global.prefix + "§4プレイヤーでないと実行できません§r");
            return true;
        }
        if(player.hasPermission("red.man10.mhat")){
            ItemStack hand = player.getInventory().getItemInMainHand();
            ItemStack helmet = player.getInventory().getHelmet();
            player.getInventory().setItemInMainHand(helmet);
            player.getInventory().setHelmet(hand);
            player.updateInventory();
            player.sendMessage(Global.prefix + "§aアイテムを頭にかぶりました§r");
        } else {
            player.sendMessage(Global.prefix + "§4あなたは権限を持っていません§r");
        }
        return true;
    }
}

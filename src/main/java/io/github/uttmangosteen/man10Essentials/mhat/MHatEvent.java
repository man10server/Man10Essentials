package io.github.uttmangosteen.man10Essentials.mhat;

import io.github.uttmangosteen.man10Essentials.Global;
import io.github.uttmangosteen.man10Essentials.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public final class MHatEvent implements Listener {

    private final Main plugin;

    public MHatEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClickHead(InventoryClickEvent event) {
        if (!plugin.settings().isEnabled("mhat")) {
            return;
        }

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (event.getSlotType() != InventoryType.SlotType.ARMOR) {
            return;
        }

        if (event.getRawSlot() != 5) {
            return;
        }

        ItemStack cursor = player.getItemOnCursor();

        if (cursor.getType() == Material.AIR) {
            return;
        }

        if (cursor.getType().getEquipmentSlot() == EquipmentSlot.HEAD) {
            return;
        }

        if (!player.hasPermission("red.man10.mhat")) {
            player.sendMessage(Global.PREFIX + "§cあなたは権限を持っていません");
            return;
        }

        ItemStack helmet = player.getInventory().getHelmet();

        player.setItemOnCursor(helmet);
        player.getInventory().setHelmet(cursor);

        event.setCancelled(true);
        player.sendMessage(Global.PREFIX + "§aアイテムを頭にかぶりました");
    }
}
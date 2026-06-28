package io.github.uttmangosteen.man10Essentials.checkOp;

import io.github.uttmangosteen.man10Essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class CheckOPEvent implements Listener {

    private final Main plugin;

    public CheckOPEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        if (!plugin.settings().isEnabled("opcheck")) {
            return;
        }

        Player player = event.getPlayer();

        if (!player.isOp()) {
            return;
        }

        if (player.hasPermission("group.gm")) {
            return;
        }

        player.setOp(false);

        String message = "不正なOPを検知したため権限を剝奪しました 対象者:" + player.getName();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "report " + message);
        plugin.getLogger().info(message);
    }
}
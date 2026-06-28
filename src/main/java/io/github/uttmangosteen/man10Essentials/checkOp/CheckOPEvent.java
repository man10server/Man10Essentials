package io.github.uttmangosteen.man10Essentials.checkOp;

import io.github.uttmangosteen.man10Essentials.Global;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static io.github.uttmangosteen.man10Essentials.Main.plugin;

public class CheckOPEvent implements Listener {
    @EventHandler
    public void onLogin(PlayerJoinEvent e) {
        if (!Global.enabled_opcheck) return;
        Player p = e.getPlayer();
        if (!p.isOp() || p.hasPermission("group.gm")) return;
        p.setOp(false);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "report 不正なOPを検知したため権限を剝奪しました 対象者:" + p.getName());
        plugin.getLogger().info("不正なOPを検知したため権限を剝奪しました 対象者:" + p.getName());
    }
}

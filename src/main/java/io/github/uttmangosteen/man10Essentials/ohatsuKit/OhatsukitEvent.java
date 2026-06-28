package io.github.uttmangosteen.man10Essentials.ohatsuKit;

import io.github.uttmangosteen.man10Essentials.Main;
import net.william278.husksync.event.BukkitPreSyncEvent;
import net.william278.husksync.event.BukkitSyncCompleteEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class OhatsukitEvent implements Listener {

    private final Main plugin;
    private final Set<UUID> knownPlayers = new HashSet<>();

    public OhatsukitEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPreSync(BukkitPreSyncEvent event) {
        knownPlayers.add(event.getUser().getUuid());
    }

    @EventHandler
    public void onSyncComplete(BukkitSyncCompleteEvent event) {
        if (!plugin.settings().isEnabled("ohatsukit")) {
            return;
        }

        Player player = Bukkit.getPlayer(event.getUser().getUuid());

        if (player == null) {
            return;
        }

        boolean isFirstLogin = !knownPlayers.contains(player.getUniqueId());

        if (!isFirstLogin) {
            knownPlayers.remove(player.getUniqueId());
            return;
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ohatsukit give " + player.getName());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rediseconomy:bal " + player.getName() + " vault give 5000");

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.sendMessage("§e§l国王様より初期装備が下賜された！");
            player.sendMessage("§e§lはじめてのログインです。電子マネー5000円と現金1500円をもらいました! /bank と入力すると電子マネーや銀行口座を確認したり、現金と交換できます。");
        }, 20L);

        knownPlayers.remove(player.getUniqueId());
    }
}
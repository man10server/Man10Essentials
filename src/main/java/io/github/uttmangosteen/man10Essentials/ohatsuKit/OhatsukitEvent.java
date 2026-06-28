package io.github.uttmangosteen.man10Essentials.ohatsuKit;

import io.github.uttmangosteen.man10Essentials.Main;
import net.william278.husksync.event.BukkitPreSyncEvent;
import net.william278.husksync.event.BukkitSyncCompleteEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import java.util.ArrayList;
import java.util.UUID;

public class OhatsukitEvent implements Listener {

    private final ArrayList<UUID> unNewbies=new ArrayList<>();

    @EventHandler
    public void onPreSync(BukkitPreSyncEvent e){
        unNewbies.add(e.getUser().getUuid());
    }

    @EventHandler
    public void onSyncComplete(BukkitSyncCompleteEvent e){
        Player player=Bukkit.getPlayer(e.getUser().getUuid());
        if(player==null)return;
        if(!unNewbies.contains(player.getUniqueId())){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ohatsukit give " + player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rediseconomy:bal " + player.getName()+" vault give 5000");
            Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
                player.sendMessage("§e§l国王様より初期装備が下賜された！");
                player.sendMessage("§e§lはじめてのログインです。電子マネー5000円と現金1500円をもらいました! /bank と入力すると電子マネーや銀行口座を確認したり、現金と交換できます。");
            },20L);
        }
        unNewbies.remove(player.getUniqueId());
    }
}

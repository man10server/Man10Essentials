package io.github.uttmangosteen.man10Essentials.whitelist;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MWhitelist {
    public MWhitelist(JavaPlugin plugin) {
        boolean mode = Bukkit.hasWhitelist();
        Bukkit.setWhitelist(true);
        plugin.getLogger().info("whitelistをonにしました");
        if (mode) return;
        int waitSeconds = plugin.getConfig().getInt("mwhitelist.waitSeconds", 60);
        if (waitSeconds <= 0) return;
        plugin.getLogger().info(waitSeconds + "秒後にwhitelistがOFFになります");
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Bukkit.setWhitelist(false);
            plugin.getLogger().info("whitelistをoffにしました");
        }, waitSeconds * 20L);
    }
}

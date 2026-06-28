package io.github.uttmangosteen.man10Essentials.whitelist;

import io.github.uttmangosteen.man10Essentials.Main;
import org.bukkit.Bukkit;

public final class MWhitelist {

    public MWhitelist(Main plugin) {
        if (!plugin.settings().isEnabled("mwhitelist")) {
            plugin.getLogger().info("起動時whitelist制御は無効です");
            return;
        }

        boolean alreadyEnabled = Bukkit.hasWhitelist();

        Bukkit.setWhitelist(true);
        plugin.getLogger().info("whitelistをonにしました");

        if (alreadyEnabled) {
            return;
        }

        int waitSeconds = plugin.getConfig().getInt("mwhitelist.waitSeconds", 60);

        if (waitSeconds <= 0) {
            return;
        }

        plugin.getLogger().info(waitSeconds + "秒後にwhitelistがOFFになります");

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Bukkit.setWhitelist(false);
            plugin.getLogger().info("whitelistをoffにしました");
        }, waitSeconds * 20L);
    }
}
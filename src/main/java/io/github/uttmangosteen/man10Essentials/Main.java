package io.github.uttmangosteen.man10Essentials;

import io.github.uttmangosteen.man10Essentials.ec.ECCommand;
import io.github.uttmangosteen.man10Essentials.mhat.MHatCommand;
import io.github.uttmangosteen.man10Essentials.checkOp.CheckOPEvent;
import io.github.uttmangosteen.man10Essentials.whitelist.MWhitelist;
import io.github.uttmangosteen.man10Essentials.ohatsuKit.OhatsukitCommand;
import io.github.uttmangosteen.man10Essentials.mhat.MHatEvent;
import io.github.uttmangosteen.man10Essentials.ohatsuKit.OhatsukitEvent;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {
    public static JavaPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();

        new MWhitelist(this);

        Global.enabled_give_ohatsukit = getConfig().getBoolean("ohatsukit.mode", false);
        Objects.requireNonNull(getCommand("ohatsukit")).setExecutor(new OhatsukitCommand(this));
        getServer().getPluginManager().registerEvents(new OhatsukitEvent(), this);

        Objects.requireNonNull(getCommand("ec")).setExecutor(new ECCommand());

        Objects.requireNonNull(getCommand("mhat")).setExecutor(new MHatCommand());
        getServer().getPluginManager().registerEvents(new MHatEvent(), this);

        Global.enabled_opcheck = getConfig().getBoolean("opcheck.mode", false);
        getServer().getPluginManager().registerEvents(new CheckOPEvent(), this);
    }
}

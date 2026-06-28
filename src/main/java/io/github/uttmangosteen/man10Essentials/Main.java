package io.github.uttmangosteen.man10Essentials;

import io.github.uttmangosteen.man10Essentials.checkOp.CheckOPEvent;
import io.github.uttmangosteen.man10Essentials.ec.ECCommand;
import io.github.uttmangosteen.man10Essentials.mhat.MHatCommand;
import io.github.uttmangosteen.man10Essentials.mhat.MHatEvent;
import io.github.uttmangosteen.man10Essentials.ohatsuKit.OhatsukitCommand;
import io.github.uttmangosteen.man10Essentials.ohatsuKit.OhatsukitEvent;
import io.github.uttmangosteen.man10Essentials.whitelist.MWhitelist;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private Settings settings;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        settings = new Settings(this);

        new MWhitelist(this);

        registerCommand("meadmin", new Man10EssentialsCommand(this));
        registerCommand("ohatsukit", new OhatsukitCommand(this));
        registerCommand("ec", new ECCommand(this));
        registerCommand("mhat", new MHatCommand(this));

        getServer().getPluginManager().registerEvents(new OhatsukitEvent(this), this);
        getServer().getPluginManager().registerEvents(new MHatEvent(this), this);
        getServer().getPluginManager().registerEvents(new CheckOPEvent(this), this);
    }

    public Settings settings() {
        return settings;
    }

    private void registerCommand(String name, org.bukkit.command.CommandExecutor executor) {
        PluginCommand command = getCommand(name);

        if (command == null) {
            getLogger().warning("plugin.yml にコマンドが定義されていません: " + name);
            return;
        }

        command.setExecutor(executor);
    }
}

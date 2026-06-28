package io.github.uttmangosteen.man10Essentials;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Settings {

    private final Main plugin;

    private final Map<String, String> featurePaths = new LinkedHashMap<>() {{
        put("ec", "features.ec.enabled");
        put("mhat", "features.mhat.enabled");
        put("ohatsukit", "features.ohatsukit.enabled");
        put("opcheck", "features.opcheck.enabled");
        put("mwhitelist", "features.mwhitelist.enabled");
    }};

    public Settings(Main plugin) {
        this.plugin = plugin;
        migrateOldConfig();
    }

    public boolean isEnabled(String key) {
        String path = featurePaths.get(key.toLowerCase());
        return path != null && plugin.getConfig().getBoolean(path, false);
    }

    public boolean setEnabled(String key, boolean enabled) {
        String path = featurePaths.get(key.toLowerCase());
        if (path == null) {
            return false;
        }

        plugin.getConfig().set(path, enabled);
        plugin.saveConfig();
        return true;
    }

    public boolean toggle(String key) {
        boolean next = !isEnabled(key);
        setEnabled(key, next);
        return next;
    }

    public Map<String, Boolean> getFeatureStatuses() {
        Map<String, Boolean> statuses = new LinkedHashMap<>();

        for (String key : featurePaths.keySet()) {
            statuses.put(key, isEnabled(key));
        }

        return statuses;
    }

    public boolean exists(String key) {
        return featurePaths.containsKey(key.toLowerCase());
    }

    private void migrateOldConfig() {
        FileConfiguration config = plugin.getConfig();

        copyIfMissing(config, "features.ohatsukit.enabled", "ohatsukit.mode");
        copyIfMissing(config, "features.opcheck.enabled", "opcheck.mode");

        setDefault(config, "features.ec.enabled", true);
        setDefault(config, "features.mhat.enabled", true);
        setDefault(config, "features.ohatsukit.enabled", config.getBoolean("ohatsukit.mode", false));
        setDefault(config, "features.opcheck.enabled", config.getBoolean("opcheck.mode", false));
        setDefault(config, "features.mwhitelist.enabled", true);

        plugin.saveConfig();
    }

    private void copyIfMissing(FileConfiguration config, String newPath, String oldPath) {
        if (!config.contains(newPath) && config.contains(oldPath)) {
            config.set(newPath, config.getBoolean(oldPath));
        }
    }

    private void setDefault(FileConfiguration config, String path, boolean value) {
        if (!config.contains(path)) {
            config.set(path, value);
        }
    }
}
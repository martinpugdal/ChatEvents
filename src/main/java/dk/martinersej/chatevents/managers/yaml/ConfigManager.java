package dk.martinersej.chatevents.managers.yaml;

import dk.martinersej.chatevents.managers.YamlManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager extends YamlManager {


    public ConfigManager(JavaPlugin plugin, String configName) {
        super();
        setupConfig(plugin, configName);
        load();
    }
}

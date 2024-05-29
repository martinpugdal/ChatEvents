package dk.martinersej.chatevents.managers;

import dk.martinersej.chatevents.ChatEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public interface Manager {

    interface YamlManager extends Manager {
        default void save() {
            try {
                getConfig().save(getFilePath());
            } catch (IOException ex) {
                ChatEvent.get().getLogger().warning("Could not save " + this.getClass().getSimpleName());
            }
        }

        default void load() {
            File file = new File(this.getFilePath());
            if (!file.exists()) {
                ChatEvent.get().saveResource(file.getName(), false);
            }
            setConfig(YamlConfiguration.loadConfiguration(file));
        }

        FileConfiguration getConfig();

        void setConfig(FileConfiguration fileConfiguration);

        String getFilePath();

        void setFilePath(String filePath);

        default void setupConfig(JavaPlugin instance, String fileName) {
            File file = new File(instance.getDataFolder(), fileName);
            setFilePath(file.getAbsolutePath());
            if (!file.exists()) {
                instance.saveResource(fileName, false);
            }
            setConfig(YamlConfiguration.loadConfiguration(file));
        }
    }
}

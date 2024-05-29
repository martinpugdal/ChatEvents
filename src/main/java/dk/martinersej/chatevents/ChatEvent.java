package dk.martinersej.chatevents;

import dk.martinersej.chatevents.commands.TypeItFirstEventCommand;
import dk.martinersej.chatevents.commands.ScrambleEventCommand;
import dk.martinersej.chatevents.events.MathEvent;
import dk.martinersej.chatevents.events.TypeItFirstEvent;
import dk.martinersej.chatevents.managers.yaml.ConfigManager;
import dk.martinersej.chatevents.events.ScrambleEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class ChatEvent extends JavaPlugin {

    private static ChatEvent instance;
    private ConfigManager configManager;
    private ScrambleEvent scrambleEvent;
    private TypeItFirstEvent typeItFirstEvent;
    private MathEvent mathEvent;

    public static ChatEvent get() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

//        configManager = new ConfigManager(this, "config.yml"); // FUCK DIG DIN DUMME LINJE


        registerCommands();
        registerEvents();
        registerTasks();
    }

    private void registerTasks() {
//        getTypeItFirstTask().startNextRound();
//        getScrambleTask().startNextRound();
        getMathTask().startNextRound();
    }

    private void registerEvents() {
    }

    private void registerCommands() {
        TypeItFirstEventCommand.register(this);
        ScrambleEventCommand.register(this);
    }

    @Override
    public void onDisable() {
        for (BukkitTask task : getServer().getScheduler().getPendingTasks()) {
            task.cancel();
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ScrambleEvent getScrambleTask() {
        if (scrambleEvent == null) {
            scrambleEvent = new ScrambleEvent(this);
        }
        return scrambleEvent;
    }

    public TypeItFirstEvent getTypeItFirstTask() {
        if (typeItFirstEvent == null) {
            typeItFirstEvent = new TypeItFirstEvent(this);
        }
        return typeItFirstEvent;
    }
    
    public MathEvent getMathTask() {
        if (mathEvent == null) {
            mathEvent = new MathEvent(this);
        }
        return mathEvent;
    }
}

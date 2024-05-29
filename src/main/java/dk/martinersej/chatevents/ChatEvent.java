package dk.martinersej.chatevents;

import dk.martinersej.chatevents.events.IEvent;
import dk.martinersej.chatevents.events.MathEvent;
import dk.martinersej.chatevents.events.TypeItFirstEvent;
import dk.martinersej.chatevents.events.ScrambleEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public final class ChatEvent extends JavaPlugin {

    private static ChatEvent instance;
    private IEvent event;

    public static ChatEvent get() {
        return instance;
    }

    public IEvent getEvent() {
        return event;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        startRandomEvent();
    }

    private void startRandomEvent() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (event != null) {
                    event.cancel();
                }
                int random = (int) (Math.random() * 3);
                switch (random) {
                    case 0:
                        event = new ScrambleEvent();
                        break;
                    case 1:
                        event = new TypeItFirstEvent();
                        break;
                    case 2:
                        event = new MathEvent();
                        break;
                }
            }
        }.runTaskTimer(this, 0, 20 * 15);
    }

    @Override
    public void onDisable() {
        for (BukkitTask task : getServer().getScheduler().getPendingTasks()) {
            task.cancel();
        }
    }
}

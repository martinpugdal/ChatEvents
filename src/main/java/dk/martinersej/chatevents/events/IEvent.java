package dk.martinersej.chatevents.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public interface IEvent extends Listener {

    BukkitRunnable c();

    @EventHandler
    void onChat(AsyncPlayerChatEvent event);

    void winnerFound(Player player);

    void sendNoOneGuessed();

    BukkitTask startCountdown();

    void start();

    void run();

    void cancelEvent();

    int getCooldownTime();

    boolean isRunning();
}
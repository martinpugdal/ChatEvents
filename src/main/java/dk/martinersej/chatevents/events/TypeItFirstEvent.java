package dk.martinersej.chatevents.events;

import dk.martinersej.chatevents.ChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TypeItFirstEvent extends BukkitRunnable implements IEvent {

    private final JavaPlugin plugin;
    private BukkitTask cooldown;
    private String word;

    public TypeItFirstEvent(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public BukkitRunnable c() {
        return new TypeItFirstEvent(plugin);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getMessage().equalsIgnoreCase(word)) {
            winnerFound(event.getPlayer());
            startNextRound();
        }
    }

    public void winnerFound(Player player) {
        cooldown.cancel();
        plugin.getServer().broadcastMessage(player.getName() + " gættede ordet!");
    }

    public void sendNoOneGuessed() {

    }

    public BukkitTask startCountdown() {
        return new BukkitRunnable() {
            int time = 15;

            @Override
            public void run() {
                if (time == 0) {
                    cancel();
                    sendNoOneGuessed();
                    startNextRound();
                    return;
                }
                time--;
            }

        }.runTaskTimer(plugin, 0, 20);
    }

    public void startNextRound() {
        try {
            cancel();
        } catch (IllegalStateException ignored) {}
        ChatEvent.get().getTypeItFirstTask().c().runTaskLaterAsynchronously(plugin, 20 * 10); // 5 minutes
    }

    public void start() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        newWord();
    }

    private void newWord() {
        List<String> words = new ArrayList<>(Arrays.asList(
                "hej",
                "med",
                "dig",
                "martin",
                "er",
                "sej"
        ));
        Collections.shuffle(words);
        word = words.get(0);
    }

    @Override
    public void run() {
        start();
        plugin.getServer().broadcastMessage("Skriv ordet først: " + word);
        cooldown = startCountdown();
    }

    public void stop() {
        plugin.getServer().broadcastMessage("TypeItFirstEvent stopped");
        cancel();
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        HandlerList.unregisterAll(this);
        if (cooldown != null) {
            cooldown.cancel();
        }
        super.cancel();
    }
}

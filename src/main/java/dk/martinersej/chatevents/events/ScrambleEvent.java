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

public class ScrambleEvent extends BukkitRunnable implements IEvent {

    private final JavaPlugin plugin;
    private BukkitTask cooldown;
    private String word;

    public ScrambleEvent(JavaPlugin plugin) {
        this.plugin = plugin;
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

    @Override
    public void run() {
        start();
        plugin.getServer().broadcastMessage("Gæt ordet: " + scramble(word));
        cooldown = startCountdown();
    }

    public BukkitTask startCountdown() {
        return new BukkitRunnable() {
            int time = 10;

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
        ChatEvent.get().getScrambleTask().c().runTaskLaterAsynchronously(plugin, 20 * 60 * 5); // 5 minutes
    }

    public BukkitRunnable c() {
        return new ScrambleEvent(plugin);
    }

    public void sendNoOneGuessed() {
        plugin.getServer().broadcastMessage("Ingen gættede ordet!");
        plugin.getServer().broadcastMessage("Ordet var: " + word);
    }

    private String scramble(String word) {
        List<String> letters = Arrays.asList(word.split(""));
        Collections.shuffle(letters);
        if (String.join("", letters).equals(word)) {
            return scramble(word);
        } else {
            return String.join("", letters);
        }
    }

    public void start() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        newWord();
    }

    public void stop() {
        plugin.getServer().broadcastMessage("ScrambleEvent stopped");
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
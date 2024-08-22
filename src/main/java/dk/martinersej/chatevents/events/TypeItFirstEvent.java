package dk.martinersej.chatevents.events;

import dk.martinersej.chatevents.hooks.CoinsHook;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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

    private BukkitTask cooldown;
    private String word;

    public BukkitRunnable c() {
        return new TypeItFirstEvent();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getMessage().equalsIgnoreCase(word)) {
            winnerFound(event.getPlayer());
        }
    }

    public void winnerFound(Player player) {
        cancelEvent();
        Bukkit.getServer().broadcastMessage(player.getName() + " gættede ordet!");
        double randomCoins = Math.random() * 75 + 25; // 25-100
        CoinsHook.addCoins(player, randomCoins);
    }

    public void sendNoOneGuessed() {
        Bukkit.getServer().broadcastMessage("Ingen kunne skrive ordet først: " + word);
    }

    public BukkitTask startCountdown() {
        return new BukkitRunnable() {
            int time = getCooldownTime();

            @Override
            public void run() {
                if (time == 0) {
                    cancelEvent();
                    sendNoOneGuessed();
                    return;
                }
                time--;
            }

        }.runTaskTimer(JavaPlugin.getProvidingPlugin(getClass()), 0, 20);
    }

    public void start() {
        Bukkit.getServer().getPluginManager().registerEvents(this, JavaPlugin.getProvidingPlugin(getClass()));
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
        Bukkit.getServer().broadcastMessage("Skriv ordet først: " + word);
        cooldown = startCountdown();
    }

    @Override
    public synchronized void cancelEvent() throws IllegalStateException {
        HandlerList.unregisterAll(this);
        if (cooldown != null) {
            cooldown.cancel();
        }
        if (isRunning()) {
            super.cancel();
        }
    }

    @Override
    public int getCooldownTime() {
        return 5;
    }

    @Override
    public boolean isRunning() {
        try {
            return super.getTaskId() != -1;
        } catch (IllegalStateException e) {
            return false;
        }
    }
}

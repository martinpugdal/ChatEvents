package dk.martinersej.chatevents.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ScrambleEventCommand extends dk.martinersej.coinsystem.utils.command.Command implements CommandExecutor {

    public ScrambleEventCommand(JavaPlugin plugin) {
        super(plugin);
    }

    public static void register(JavaPlugin plugin) {
        plugin.getCommand("scrambleevent").setExecutor(new TypeItFirstEventCommand(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}

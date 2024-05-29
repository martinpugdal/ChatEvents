package dk.martinersej.chatevents.commands;

import dk.martinersej.chatevents.commands.subchatevent.TypeItFirstEventStart;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class TypeItFirstEventCommand extends dk.martinersej.coinsystem.utils.command.Command implements CommandExecutor {

    public TypeItFirstEventCommand(JavaPlugin plugin) {
        super(plugin);

        super.addSubCommand(new TypeItFirstEventStart(plugin));
    }

    public static void register(JavaPlugin plugin) {
        plugin.getCommand("typeitfirstevent").setExecutor(new TypeItFirstEventCommand(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}

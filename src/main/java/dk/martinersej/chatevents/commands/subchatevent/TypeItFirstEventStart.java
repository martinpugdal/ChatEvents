package dk.martinersej.chatevents.commands.subchatevent;

import dk.martinersej.coinsystem.utils.command.CommandResult;
import dk.martinersej.coinsystem.utils.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class TypeItFirstEventStart extends SubCommand {
    public TypeItFirstEventStart(JavaPlugin plugin) {
        super(plugin, "Starts a chat event", "start", "chatevents.typeitfirst.start", "start");
    }

    @Override
    public CommandResult execute(CommandSender commandSender, String[] strings) {
        return null;
    }
}

package dk.martinersej.chatevents.commands.subscrambleevent;

import dk.martinersej.coinsystem.utils.command.CommandResult;
import dk.martinersej.coinsystem.utils.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ScrambleEventStart extends SubCommand {

    public ScrambleEventStart(JavaPlugin plugin) {
        super(plugin, "Starts a scramble event", "start", "chatevents.scramble.start", "start");
    }

    @Override
    public CommandResult execute(CommandSender commandSender, String[] strings) {
        return null;
    }
}

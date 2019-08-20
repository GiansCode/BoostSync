package io.samdev.boostsync.plugin.command;

import io.samdev.boostsync.plugin.BoostSync;
import io.samdev.boostsync.plugin.util.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LinkCommand implements CommandExecutor
{
	private final BoostSync plugin;

	public LinkCommand(BoostSync plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (!(sender instanceof Player))
		{
			Message.COMMAND_LINK_PLAYER_ONLY.send(sender);
			return true;
		}

		Player player = (Player) sender;

		if (args.length != 0)
		{
			Message.COMMAND_LINK_USAGE.send(player);
			return true;
		}

		if (plugin.getSyncManager().isSynced(player))
		{
			Message.COMMAND_LINK_ALREADY_SYNCED.send(player);
			return true;
		}

		plugin.getCodeManager().generateCode(player, code ->
			Message.COMMAND_LINK_SUCCESS.send(player, "code", code)
		);

		return true;
	}
}

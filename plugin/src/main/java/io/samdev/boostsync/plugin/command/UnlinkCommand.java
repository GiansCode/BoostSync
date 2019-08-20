package io.samdev.boostsync.plugin.command;

import io.samdev.boostsync.plugin.BoostSync;
import io.samdev.boostsync.plugin.util.Message;
import io.samdev.boostsync.plugin.util.Scheduling;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnlinkCommand implements CommandExecutor
{
	private final BoostSync plugin;

	public UnlinkCommand(BoostSync plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (!sender.hasPermission(Message.COMMAND_UNLINK_PERMISSION.toString()))
		{
			Message.COMMAND_UNLINK_NO_PERMISSION.send(sender);
			return true;
		}

		if (args.length != 1)
		{
			Message.COMMAND_UNLINK_USAGE.send(sender);
			return true;
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

		plugin.getSqlDatabase().fetchSyncData(
			target.getUniqueId()
		).thenAccept(data -> Scheduling.sync(() ->
		{
			if (data == null)
			{
				Message.COMMAND_UNLINK_NOT_SYNCED.send(sender);
				return;
			}

			plugin.getSqlDatabase().deleteSyncData(target.getUniqueId());

			if (target.isOnline())
			{
				plugin.getSyncManager().removePlayer(target.getPlayer());
			}

			Message.COMMAND_UNLINK_SUCCESS.send(sender, "player", target.getName());
		}));

		return true;
	}
}

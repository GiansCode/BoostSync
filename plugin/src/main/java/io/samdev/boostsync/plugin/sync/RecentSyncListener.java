package io.samdev.boostsync.plugin.sync;

import io.samdev.boostsync.plugin.BoostSync;
import io.samdev.boostsync.plugin.util.Message;
import io.samdev.boostsync.plugin.util.Scheduling;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RecentSyncListener
{
	private final BoostSync plugin;

	public RecentSyncListener(BoostSync plugin)
	{
		this.plugin = plugin;

		int pollingInterval = plugin.getConfig().getInt("sync.polling_interval");
		Bukkit.getScheduler().runTaskTimer(plugin, this::checkNewSync, 0L, pollingInterval * 20L);
	}

	private void checkNewSync()
	{
		for (Player player : Bukkit.getOnlinePlayers())
		{
			if (plugin.getSyncManager().isSynced(player))
			{
				return;
			}

			plugin.getSqlDatabase().fetchSyncData(
				player.getUniqueId()
			).thenAccept(data -> Scheduling.sync(() ->
			{
				if (data != null)
				{
					plugin.getSyncManager().handleJoin(player, data);
					Bukkit.getPluginManager().callEvent(new DiscordAccountSyncEvent(player, data));

					Message.ACCOUNT_LINKED.send(player);
				}
			}));
		}
	}
}

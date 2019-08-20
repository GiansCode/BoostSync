package io.samdev.boostsync.plugin.sync;

import io.samdev.boostsync.common.database.BoostSyncDatabase;
import io.samdev.boostsync.plugin.util.Scheduling;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

class SyncLoginListener implements Listener
{
	private final SyncManager manager;
	private final BoostSyncDatabase database;

	SyncLoginListener(SyncManager manager, BoostSyncDatabase database)
	{
		this.manager = manager;
		this.database = database;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(PlayerLoginEvent event)
	{
		if (event.getResult() != Result.ALLOWED)
		{
			return;
		}

		database.fetchSyncData(
			event.getPlayer().getUniqueId()
		).thenAccept(data -> Scheduling.sync(() ->
			manager.handleJoin(event.getPlayer(), data)
		));
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event)
	{
		manager.handleQuit(event.getPlayer());
	}
}

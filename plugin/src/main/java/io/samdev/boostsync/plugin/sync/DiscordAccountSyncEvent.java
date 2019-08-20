package io.samdev.boostsync.plugin.sync;

import io.samdev.boostsync.common.SyncData;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class DiscordAccountSyncEvent extends PlayerEvent
{
	private final SyncData syncData;

	public DiscordAccountSyncEvent(Player player, SyncData syncData)
	{
		super(player);

		this.syncData = syncData;
	}

	public SyncData getSyncData()
	{
		return syncData;
	}

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}

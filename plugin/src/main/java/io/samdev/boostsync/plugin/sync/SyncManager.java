package io.samdev.boostsync.plugin.sync;

import io.samdev.boostsync.common.SyncData;
import io.samdev.boostsync.common.database.BoostSyncDatabase;
import io.samdev.boostsync.plugin.BoostSync;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SyncManager
{
	private final BoostSyncDatabase database;
	private final Map<Player, SyncData> syncData;

	public SyncManager(BoostSync plugin)
	{
		this.database = plugin.getSqlDatabase();
		this.syncData = new HashMap<>();

		Bukkit.getPluginManager().registerEvents(new SyncLoginListener(this, database), plugin);
		new RecentSyncListener(plugin);
	}

	public boolean isSynced(Player player)
	{
		return getSyncData(player) != null;
	}

	public Map<Player, SyncData> getSyncDatas()
	{
		return Collections.unmodifiableMap(syncData);
	}

	public SyncData getSyncData(Player player)
	{
		return syncData.get(player);
	}

	void handleJoin(Player player, SyncData data)
	{
		if (data != null)
		{
			syncData.put(player, data);
		}
	}

	void handleQuit(Player player)
	{
		syncData.remove(player);
	}

	public void removePlayer(Player player)
	{
		syncData.remove(player);
	}
}

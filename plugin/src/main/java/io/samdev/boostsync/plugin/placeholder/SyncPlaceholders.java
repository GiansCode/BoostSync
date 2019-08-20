package io.samdev.boostsync.plugin.placeholder;

import io.samdev.boostsync.common.SyncData;
import io.samdev.boostsync.plugin.BoostSync;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class SyncPlaceholders extends PlaceholderExpansion
{
	private final BoostSync plugin;

	public SyncPlaceholders(BoostSync plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public String getIdentifier()
	{
		return "boostsync";
	}

	@Override
	public String onPlaceholderRequest(Player player, String identifier)
	{
		if (player == null)
		{
			plugin.getLogger().warning("attempted to use placeholder with no player");
			return null;
		}

		SyncData syncData = plugin.getSyncManager().getSyncData(player);

		if (identifier.equalsIgnoreCase("synced"))
		{
			return String.valueOf(syncData != null);
		}

		if (identifier.equalsIgnoreCase("boosting"))
		{
			return String.valueOf(syncData != null && syncData.isBoosting());
		}

		plugin.getLogger().warning("attempted to use invalid placeholder '" + identifier + "'");
		return null;
	}

	@Override
	public String getAuthor()
	{
		return plugin.getDescription().getAuthors().get(0);
	}

	@Override
	public String getVersion()
	{
		return plugin.getDescription().getVersion();
	}

	@Override
	public boolean persist()
	{
		return true;
	}

	@Override
	public boolean canRegister()
	{
		return true;
	}
}

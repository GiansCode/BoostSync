package io.samdev.boostsync.plugin.util;

import io.samdev.boostsync.plugin.BoostSync;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Scheduling
{
	private static final BoostSync plugin = JavaPlugin.getPlugin(BoostSync.class);

	public static void sync(Runnable runnable)
	{
		Bukkit.getScheduler().runTask(plugin, runnable);
	}

	public static void async(Runnable runnable)
	{
		Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
	}
}

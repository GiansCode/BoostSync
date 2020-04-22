package io.samdev.boostsync.plugin;

import io.samdev.actionutil.ActionUtil;
import io.samdev.boostsync.common.database.BoostSyncDatabase;
import io.samdev.boostsync.common.database.SqlCredentials;
import io.samdev.boostsync.plugin.command.LinkCommand;
import io.samdev.boostsync.plugin.command.UnlinkCommand;
import io.samdev.boostsync.plugin.placeholder.SyncPlaceholders;
import io.samdev.boostsync.plugin.reward.RewardManager;
import io.samdev.boostsync.plugin.sync.CodeManager;
import io.samdev.boostsync.plugin.sync.SyncManager;
import io.samdev.boostsync.plugin.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BoostSync extends JavaPlugin
{
	private BoostSyncDatabase database;
	private SyncManager syncManager;
	private CodeManager codeManager;
	
	private ActionUtil actionUtil;
	
	@Override
	public void onEnable()
	{
		saveDefaultConfig();

		Message.init(this);

		database = new BoostSyncDatabase(constructDatabaseCredentials());

		syncManager = new SyncManager(this);
		codeManager = new CodeManager(this);

		actionUtil = ActionUtil.init(this);
		
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
		{
			new SyncPlaceholders(this).register();
		}

		getCommand("link").setExecutor(new LinkCommand(this));
		getCommand("unlink").setExecutor(new UnlinkCommand(this));

		new RewardManager(this);
	}

	private SqlCredentials constructDatabaseCredentials()
	{
		return new SqlCredentials(
			getConfig().getString("database.host"),
			getConfig().getInt("database.port"),
			getConfig().getString("database.database"),
			getConfig().getString("database.username"),
			getConfig().getString("database.password")
		);
	}

	public BoostSyncDatabase getSqlDatabase()
	{
		return database;
	}

	public SyncManager getSyncManager()
	{
		return syncManager;
	}

	public CodeManager getCodeManager()
	{
		return codeManager;
	}

	public ActionUtil getActionUtil() {
		return actionUtil;
	}
}

package io.samdev.boostsync.bot;

import io.samdev.boostsync.bot.boost.BoostManager;
import io.samdev.boostsync.bot.command.CommandListener;
import io.samdev.boostsync.common.database.BoostSyncDatabase;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;

import static io.samdev.boostsync.bot.util.Logging.info;
import static io.samdev.boostsync.bot.util.Logging.severe;

public class BoostSync
{
	public static void main(String[] args)
	{
		new BoostSync();
	}

	private BoostSync()
	{
		if (!loadConfig())
		{
			severe("Unable to load config");
			return;
		}

		if (!connectToDatabase())
		{
			severe("Unable to connect to database");
			return;
		}

		if (!connectToDiscord())
		{
			severe("Unable to connect to Discord");
		}

		new CommandListener(this);
		boostManager = new BoostManager(this);
	}

	private BoostManager boostManager;

	public BoostManager getBoostManager()
	{
		return boostManager;
	}

	private BotConfig config;

	public BotConfig getConfig()
	{
		return config;
	}

	private boolean loadConfig()
	{
		config = new BotConfig(this);
		return config.init();
	}

	private BoostSyncDatabase database;

	public BoostSyncDatabase getDatabase()
	{
		return database;
	}

	private boolean connectToDatabase()
	{
		try
		{
			database = new BoostSyncDatabase(config.getDatabaseCredentials());
			return true;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}

	private JDA jda;

	public JDA getJda()
	{
		return jda;
	}

	private Guild guild;

	public Guild getGuild()
	{
		return guild;
	}

	private boolean ready = false;

	private boolean connectToDiscord()
	{
		try
		{
			jda = new JDABuilder(AccountType.BOT)
				.setToken(config.getBotToken())
				.build();

			jda.awaitReady();

			info("Connected to bot " + jda.getSelfUser().getAsTag());
			guild = jda.getGuildById(config.getGuildId());

			return true;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}
}

package io.samdev.boostsync.bot;

import com.google.gson.JsonObject;
import io.samdev.boostsync.bot.util.Message;
import io.samdev.boostsync.bot.util.UtilFile;
import io.samdev.boostsync.bot.util.UtilJson;
import io.samdev.boostsync.common.database.SqlCredentials;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.File;

import static io.samdev.boostsync.bot.util.Logging.info;
import static io.samdev.boostsync.bot.util.Logging.severe;

public class BotConfig
{
	private final BoostSync bot;

	BotConfig(BoostSync bot)
	{
		this.bot = bot;
	}

	public String getBotToken()
	{
		return config.get("bot_token").getAsString();
	}

	public String getGuildId()
	{
		return config.get("guild_id").getAsString();
	}

	public SqlCredentials getDatabaseCredentials()
	{
		JsonObject dbConfig = config.getAsJsonObject("database");

		return new SqlCredentials(
			dbConfig.get("host").getAsString(),
			dbConfig.get("port").getAsInt(),
			dbConfig.get("database").getAsString(),
			dbConfig.get("username").getAsString(),
			dbConfig.get("password").getAsString()
		);
	}

	public TextChannel getBotChannel()
	{
		return bot.getJda().getTextChannelById(config.get("bot_channel_id").getAsString());
	}

	public Role getBoostingRole()
	{
		return bot.getGuild().getRoleById(config.get("boosting_role_id").getAsString());
	}

	public String getSyncCommand()
	{
		return config.get("sync_command").getAsString();
	}

	private JsonObject config;

	boolean init()
	{
		File file = new File("config.json");

		if (!file.exists())
		{
			UtilFile.copyResource("config.json");
			info("Created config file - please enter information and re-run the bot");

			return false;
		}

		config = UtilJson.parse(file);

		if (config == null)
		{
			severe("Error parsing config from file");
			return false;
		}

		Message.init(config);
		return true;
	}
}

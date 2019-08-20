package io.samdev.boostsync.bot.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.samdev.boostsync.common.util.UtilString;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import static io.samdev.boostsync.bot.util.Logging.severe;

public enum Message
{
	COMMAND_USAGE,
	COMMAND_ALREADY_SYNCED,
	COMMAND_CODE_INVALID,
	COMMAND_CODE_EXPIRED,
	COMMAND_SUCCESS
	;

	private String value;

	@Override
	public String toString()
	{
		return value;
	}

	public void send(TextChannel channel, Member member, Object... params)
	{
		if (value != null && !value.isEmpty())
		{
			params = UtilArray.prepend(params, "tag", member.getAsMention());

			channel.sendMessage(UtilString.formatArgs(value, params)).queue();
		}
	}

	public static void init(JsonObject config)
	{
		JsonObject messages = config.getAsJsonObject("messages");

		for (Message message : values())
		{
			JsonElement element = messages.get(message.name().toLowerCase());

			if (element == null)
			{
				severe("Value missing for message " + message.name());
				continue;
			}

			String value =
				element instanceof JsonPrimitive ? element.getAsJsonPrimitive().getAsString() :
				null;

			if (value == null)
			{
				severe("Invalid data type for message " + message.name());
				continue;
			}

			message.value = value;
		}
	}
}

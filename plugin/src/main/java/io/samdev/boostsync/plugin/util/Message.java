package io.samdev.boostsync.plugin.util;

import io.samdev.boostsync.common.util.UtilString;
import io.samdev.boostsync.plugin.BoostSync;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public enum Message
{
	COMMAND_LINK_USAGE,
	COMMAND_LINK_PLAYER_ONLY,
	COMMAND_LINK_ALREADY_SYNCED,
	COMMAND_LINK_SUCCESS,

	COMMAND_UNLINK_PERMISSION,
	COMMAND_UNLINK_NO_PERMISSION,
	COMMAND_UNLINK_USAGE,
	COMMAND_UNLINK_NOT_SYNCED,
	COMMAND_UNLINK_SUCCESS,

	ACCOUNT_LINKED
	;

	private String value;

	@Override
	public String toString()
	{
		return value;
	}

	public void send(CommandSender sender, Object... params)
	{
		if (value != null && !value.isEmpty())
		{
			sender.sendMessage(MiniMessage.miniMessage().deserialize(UtilString.formatArgs(value, params)));
		}
	}

	@SuppressWarnings("unchecked")
	public static void init(BoostSync plugin)
	{
		for (Message message : values())
		{
			String value;

			if (plugin.getConfig().isList("messages." + message.name().toLowerCase())) {
				value = String.join("\n", plugin.getConfig().getStringList("messages." + message.name().toLowerCase()));
			} else {
				value = plugin.getConfig().getString("messages." + message.name().toLowerCase());
			}

			if (value == null)
			{
				plugin.getLogger().severe("invalid data type for message " + message.name());
				continue;
			}

			message.value = value;
		}
	}
}

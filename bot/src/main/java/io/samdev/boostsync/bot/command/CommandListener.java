package io.samdev.boostsync.bot.command;

import io.samdev.boostsync.bot.BoostSync;
import io.samdev.boostsync.bot.util.Message;
import io.samdev.boostsync.common.PendingData;
import io.samdev.boostsync.common.SyncData;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter
{
	private final BoostSync bot;

	public CommandListener(BoostSync bot)
	{
		this.bot = bot;

		this.botChannel = bot.getConfig().getBotChannel();
		this.syncCommand = bot.getConfig().getSyncCommand();

		bot.getJda().addEventListener(this);
	}

	private final TextChannel botChannel;
	private final String syncCommand;

	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if (event.getChannelType() != ChannelType.TEXT)
		{
			return;
		}

		TextChannel channel = (TextChannel) event.getChannel();
		Member member = event.getMember();

		if (member == null || member.getUser().isBot() || (botChannel != null && channel != botChannel))
		{
			return;
		}

		String message = event.getMessage().getContentRaw();
		String[] args = message.split(" ");

		if (args.length < 1 || !args[0].equalsIgnoreCase(syncCommand))
		{
			return;
		}

		if (args.length != 2)
		{
			Message.COMMAND_USAGE.send(channel, member);
			return;
		}

		handleValidCommand(member, channel, args[1]);
	}

	private void handleValidCommand(Member member, TextChannel channel, String code)
	{
		bot.getDatabase().fetchSyncData(
			member.getId()
		).thenAccept(syncData ->
			handleSyncData(member, channel, code, syncData)
		);
	}

	private void handleSyncData(Member member, TextChannel channel, String code, SyncData syncData)
	{
		if (syncData != null)
		{
			Message.COMMAND_ALREADY_SYNCED.send(channel, member);
			return;
		}

		bot.getDatabase().fetchPendingData(
			code
		).thenAccept(pendingData ->
			handlePendingData(member, channel, code, pendingData)
		);
	}

	private void handlePendingData(Member member, TextChannel channel, String code, PendingData pendingData)
	{
		if (pendingData == null)
		{
			Message.COMMAND_CODE_INVALID.send(channel, member);
			return;
		}

		if (pendingData.hasExpired())
		{
			Message.COMMAND_CODE_EXPIRED.send(channel, member);
			return;
		}

		bot.getDatabase().removeCode(code);

		SyncData syncData = new SyncData(member.getId(), bot.getBoostManager().isBoosting(member), 0L, false);
		bot.getDatabase().insertSyncData(pendingData.getUuid(), syncData);

		Message.COMMAND_SUCCESS.send(channel, member, "username", pendingData.getUsername());
	}
}

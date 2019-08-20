package io.samdev.boostsync.bot.boost;

import io.samdev.boostsync.bot.BoostSync;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BoostManager extends ListenerAdapter
{
	private final BoostSync bot;

	public BoostManager(BoostSync bot)
	{
		this.bot = bot;
		this.boostingRole = bot.getConfig().getBoostingRole();

		bot.getJda().addEventListener(this);
	}

	private final Role boostingRole;

	public boolean isBoosting(Member member)
	{
		return member.getRoles().contains(boostingRole);
	}

	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event)
	{
		Member member = event.getMember();

		if (member.getUser().isBot() || !event.getRoles().contains(boostingRole))
		{
			return;
		}

		updateBoostingStatus(member, true);
	}

	@Override
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event)
	{
		Member member = event.getMember();

		if (member.getUser().isBot() || !event.getRoles().contains(boostingRole))
		{
			return;
		}

		updateBoostingStatus(member, false);
	}

	private void updateBoostingStatus(Member member, boolean boosting)
	{
		bot.getDatabase().updateBoostingStatus(member.getId(), boosting);
	}
}

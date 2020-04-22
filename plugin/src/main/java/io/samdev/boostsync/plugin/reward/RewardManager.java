package io.samdev.boostsync.plugin.reward;

import io.samdev.actionutil.ActionUtil;
import io.samdev.boostsync.common.SyncData;
import io.samdev.boostsync.plugin.BoostSync;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class RewardManager
{
	private final BoostSync plugin;

	public RewardManager(BoostSync plugin)
	{
		this.plugin = plugin;

		this.oneTimeRewards = plugin.getConfig().getStringList("sync.one_time_rewards");
		this.boostRewards = plugin.getConfig().getStringList("boost.rewards");

		this.rewardCooldown = Duration.ofSeconds(plugin.getConfig().getLong("boost.reward_cooldown"));

		Bukkit.getScheduler().runTaskTimer(plugin, this::checkPlayers, 0L, 60L);
	}

	private final List<String> oneTimeRewards;
	private final List<String> boostRewards;

	private final Duration rewardCooldown;

	private void checkPlayers()
	{
		for (Map.Entry<Player, SyncData> entry : plugin.getSyncManager().getSyncDatas().entrySet())
		{
			Player player = entry.getKey();
			SyncData data = entry.getValue();

			if (!data.getOneTimeReward())
			{
				giveOneTimeRewards(player);

				data.updateOneTimeReward();
				plugin.getSqlDatabase().updateOneTimeReward(player.getUniqueId());
			}

			if (data.isBoosting() && Instant.now().minus(rewardCooldown).isAfter(data.getLastBoostReward()))
			{
				giveBoostRewards(player);

				data.updateLastBoostReward();
				plugin.getSqlDatabase().updateLastBoostReward(player.getUniqueId(), data.getLastBoostReward());
			}
		}
	}

	private void giveOneTimeRewards(Player player)
	{
		plugin.getActionUtil().executeActions(player, oneTimeRewards);
	}

	private void giveBoostRewards(Player player)
	{
		plugin.getActionUtil().executeActions(player, boostRewards);
	}
}

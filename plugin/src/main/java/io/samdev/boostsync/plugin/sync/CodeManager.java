package io.samdev.boostsync.plugin.sync;

import io.samdev.boostsync.plugin.BoostSync;
import io.samdev.boostsync.plugin.util.Scheduling;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class CodeManager
{
	private final BoostSync plugin;

	public CodeManager(BoostSync plugin)
	{
		this.plugin = plugin;
		this.expirySeconds = plugin.getConfig().getInt("code.expiry_time", 60);
	}

	private final int expirySeconds;

	public void generateCode(Player player, Consumer<String> consumer)
	{
		Scheduling.async(() ->
		{
			String code = generateNewCode();

			Instant expiry = expirySeconds == 0 ?
				Instant.ofEpochMilli(0L) :
				Instant.now().plus(Duration.ofSeconds(expirySeconds));

			plugin.getSqlDatabase().insertCode(code, expiry, player.getUniqueId(), player.getName());
			Scheduling.sync(() -> consumer.accept(code));
		});
	}

	private String generateNewCode()
	{
		String code;

		do
		{
			code = generateCode();
		}
		while (plugin.getSqlDatabase().fetchPendingDataSync(code) != null);

		return code;
	}

	private String generateCode()
	{
		return String.format(
			"%05d",
			ThreadLocalRandom.current().nextInt(100_000)
		);
	}
}

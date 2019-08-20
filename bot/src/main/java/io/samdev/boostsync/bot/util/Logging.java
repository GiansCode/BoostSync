package io.samdev.boostsync.bot.util;

import java.util.Date;

public final class Logging
{
	private Logging() {}

	public static void info(String msg)
	{
		System.out.println("[" + new Date() + "] INFO: " + msg);
	}

	public static void severe(String msg)
	{
		System.err.println("[" + new Date() + "] SEVERE: " + msg);
	}
}

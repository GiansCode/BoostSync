package io.samdev.boostsync.bot.util;

import io.samdev.boostsync.bot.BoostSync;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.samdev.boostsync.bot.util.Logging.severe;

public final class UtilFile
{
	private UtilFile() {}

	public static void copyResource(String resourceName)
	{
		try
		{
			InputStream stream = BoostSync.class.getClassLoader().getResourceAsStream(resourceName);
			Files.copy(stream, Paths.get(new File(resourceName).getAbsolutePath()));
		}
		catch (IOException ex)
		{
			severe("Unable to copy JAR resource to disk");
			ex.printStackTrace();
		}
	}
}

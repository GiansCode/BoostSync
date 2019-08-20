package io.samdev.boostsync.bot.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static io.samdev.boostsync.bot.util.Logging.severe;

public final class UtilJson
{
	private UtilJson() {}

	private static final JsonParser parser = new JsonParser();

	public static JsonObject parse(File file)
	{
		try
		{
			return parser.parse(new FileReader(file)).getAsJsonObject();
		}
		catch (IOException ex)
		{
			severe("Error reading config file");
			ex.printStackTrace();
		}

		return null;
	}
}

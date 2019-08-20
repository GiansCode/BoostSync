package io.samdev.boostsync.common.util;

public final class UtilString
{
	private UtilString() {}

	public static String formatArgs(String message, Object... params)
	{
		if (params.length == 0)
		{
			return message;
		}

		assert params.length % 2 == 0 : "string parameters are unbalanced";

		for (int i = 0; i < params.length - 1; i += 2)
		{
			message = message.replace("{" + params[i] + "}", params[i + 1].toString());
		}

		return message;
	}
}

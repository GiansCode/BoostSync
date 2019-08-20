package io.samdev.boostsync.bot.util;

public final class UtilArray
{
	private UtilArray() {}

	public static Object[] prepend(Object[] array, Object... newObjects)
	{
		Object[] newArray = new Object[array.length + newObjects.length];

		for (int i = 0; i < newObjects.length; i++)
		{
			newArray[i] = newObjects[i];
		}

		System.arraycopy(array, 0, newArray, newObjects.length, array.length);
		return newArray;
	}
}

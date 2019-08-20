package io.samdev.boostsync.common.database;

import java.sql.SQLException;

@FunctionalInterface
public interface SqlConsumer<T>
{
	void accept(T object) throws SQLException;

	static <T> SqlConsumer<T> blank()
	{
		return v -> {};
	}
}

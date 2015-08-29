package org.heat.shared.database;

import com.google.common.collect.Multimap;
import org.heat.shared.Pair;

import java.sql.Connection;
import java.sql.SQLException;

public final class Connections {
    private Connections() {}

    public static NamedPreparedStatement prepare(Connection co, String sql) throws SQLException {
        Pair<String, Multimap<String, Integer>> pair = NamedPreparedStatementImpl.parse(sql);
        return new NamedPreparedStatementImpl(co.prepareStatement(pair.first), pair.second);
    }
}

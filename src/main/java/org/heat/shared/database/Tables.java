package org.heat.shared.database;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Tables {
    private Tables() {}

    public static String createInsertQuery(Table<?> table) {
        return
            "insert into " + table.getTableName() +
            // columns
            Stream.concat(table.getPrimaryKeys().stream(), table.getInsertableColumns().stream())
                    .collect(Collectors.joining(", ", "(", ")")) + " " +
            // placeholders
            Stream.concat(table.getPrimaryKeys().stream(), table.getInsertableColumns().stream())
                    .map(c -> ":" + c)
                    .collect(Collectors.joining(", ", "values(", ")"))
            ;
    }

    public static String createUpdateQuery(Table<?> table) {
        return
            "update " + table.getTableName() + " set " +
            // { column=placeholder }[, ]+
            table.getUpdatableColumns().stream()
                    .map(column -> column + "=:" + column)
                    .collect(Collectors.joining(", ")) + " " +
            // where
            "where " +
            table.getPrimaryKeys().stream()
                    .map(column -> column + "=:" + column)
                    .collect(Collectors.joining(" AND "))
            ;
    }

    public static String createDeleteQuery(Table<?> table) {
        return
            "delete from " + table.getTableName() + " " +
            "where " +
            table.getPrimaryKeys().stream()
                .map(column -> column + "=:" + column)
                .collect(Collectors.joining("AND"))
            ;
    }

    public static String createSelectQuery(Table<?> table) {
        return
            "select " +
            Stream.concat(table.getPrimaryKeys().stream(), table.getSelectableColumns().stream())
                    .collect(Collectors.joining(", ")) + " " +
            "from " + table.getTableName()
            ;
    }
}

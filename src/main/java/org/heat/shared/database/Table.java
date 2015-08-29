package org.heat.shared.database;

import org.fungsi.concurrent.Future;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface Table<T> {
    String getTableName();

    List<String> getPrimaryKeys();

    List<String> getSelectableColumns();
    List<String> getInsertableColumns();
    List<String> getUpdatableColumns();

    void setPrimaryKeys(NamedPreparedStatement s, T val) throws SQLException;
    Future<T> importFromDb(ResultSet rset) throws SQLException;
    void insertToDb(NamedPreparedStatement s, T val) throws SQLException;
    void updateToDb(NamedPreparedStatement s, T val) throws SQLException;

    public static <T> TableBuilder<T> builder() {
        return new TableBuilder<>();
    }
}

package org.heat.shared.database;

import org.fungsi.concurrent.Future;
import org.fungsi.concurrent.Futures;
import org.fungsi.concurrent.Worker;
import org.fungsi.function.UnsafeConsumer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class JdbcRepositoryNG<T> {

    private final Table<T> table;
    private final Worker worker;

    protected JdbcRepositoryNG(Table<T> table, Worker worker) {
        this.table = table;
        this.worker = worker;
    }

    protected abstract Connection getConnection();

    protected final Table<T> getTable() {
        return table;
    }

    protected final Worker getWorker() {
        return worker;
    }

    protected String createInsertQuery() {
        return Tables.createInsertQuery(getTable());
    }

    protected String createUpdateQuery() {
        return Tables.createUpdateQuery(getTable());
    }

    protected String createDeleteQuery() {
        return Tables.createDeleteQuery(getTable());
    }

    protected String createSelectQuery() {
        return Tables.createSelectQuery(getTable());
    }

    protected String createSelectWhereQuery(String column) {
        return getSelectQuery() + " where " + column + "=:" + column;
    }

    private String insertQuery, updateQuery, deleteQuery, selectQuery;

    protected final String getInsertQuery() {
        if (insertQuery == null) {
            insertQuery = createInsertQuery();
        }
        return insertQuery;
    }

    protected final String getUpdateQuery() {
        if (updateQuery == null) {
            updateQuery = createUpdateQuery();
        }
        return updateQuery;
    }

    protected final String getDeleteQuery() {
        if (deleteQuery == null) {
            deleteQuery = createDeleteQuery();
        }
        return deleteQuery;
    }

    protected final String getSelectQuery() {
        if (selectQuery == null) {
            selectQuery = createSelectQuery();
        }
        return selectQuery;
    }

    protected T pipeline(T val) {
        return val;
    }

    protected T execute(NamedPreparedStatement statement, T val) throws SQLException {
        statement.execute();
        return val;
    }

    protected T pipelineInsert(T val) {
        return pipeline(val);
    }

    protected T executeInsert(NamedPreparedStatement statement, T val) throws SQLException {
        return execute(statement, val);
    }

    protected T pipelineUpdate(T val) {
        return pipeline(val);
    }

    protected T executeUpdate(NamedPreparedStatement statement, T val) throws SQLException {
        return execute(statement, val);
    }

    protected T pipelineDelete(T val) {
        return pipeline(val);
    }

    protected T executeDelete(NamedPreparedStatement statement, T val) throws SQLException {
        return execute(statement, val);
    }

    protected Future<T> insert(T val) {
        return getWorker().submit(() -> {
            T newVal = pipelineInsert(val);

            try (Connection connection = getConnection();
                 NamedPreparedStatement statement = Connections.prepare(connection, getInsertQuery()))
            {
                getTable().setPrimaryKeys(statement, newVal);
                getTable().insertToDb(statement, newVal);

                return executeInsert(statement, newVal);
            }
        });
    }

    protected Future<T> update(T val) {
        return getWorker().submit(() -> {
            T newVal = pipelineUpdate(val);

            try (Connection connection = getConnection();
                 NamedPreparedStatement statement = Connections.prepare(connection, getUpdateQuery()))
            {
                getTable().setPrimaryKeys(statement, newVal);
                getTable().updateToDb(statement, newVal);

                return executeUpdate(statement, newVal);
            }
        });
    }

    protected Future<T> delete(T val) {
        return getWorker().submit(() -> {
            T newVal = pipelineUpdate(val);

            try (Connection connection = getConnection();
                 NamedPreparedStatement statement = Connections.prepare(connection, getDeleteQuery()))
            {
                getTable().setPrimaryKeys(statement, newVal);

                return executeDelete(statement, newVal);
            }
        });
    }

    protected Future<T> findFirst(String sql, UnsafeConsumer<NamedPreparedStatement> fn) {
        return getWorker().execute(() -> {
            try (Connection connection = getConnection();
                 NamedPreparedStatement statement = Connections.prepare(connection, sql)) {
                fn.accept(statement);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) throw new NoSuchElementException();
                    return getTable().importFromDb(resultSet);
                }
            }
        });
    }

    protected Future<List<T>> findList(String sql, UnsafeConsumer<NamedPreparedStatement> fn) {
        return getWorker().execute(() -> {
            try (Connection connection = getConnection();
                 NamedPreparedStatement statement = Connections.prepare(connection, sql)) {
                fn.accept(statement);

                List<Future<T>> values = new ArrayList<>();
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        values.add(getTable().importFromDb(resultSet));
                    }
                }
                return Futures.collect(values);
            }
        });
    }

    // shorthands
    protected Future<T> findFirstByColumn(String column, Object param) {
        return findFirst(createSelectWhereQuery(column), s -> s.setObject(column, param));
    }

    protected Future<List<T>> findListByColumn(String column, Object param) {
        return findList(createSelectWhereQuery(column), s -> s.setObject(column, param));
    }

    protected Future<T> findFirstByIntColumn(String column, int param) {
        return findFirst(createSelectWhereQuery(column), s -> s.setInt(column, param));
    }

    protected Future<List<T>> findListByIntColumn(String column, int param) {
        return findList(createSelectWhereQuery(column), s -> s.setInt(column, param));
    }

}

package org.heat.shared.database;

import lombok.SneakyThrows;
import org.fungsi.function.UnsafeConsumer;
import org.fungsi.function.UnsafeFunction;
import org.heat.shared.NonUniqueElementException;
import org.heat.shared.stream.Streams;

import java.sql.*;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static com.google.common.base.Throwables.propagate;
import static java.util.stream.Collectors.joining;
import static org.heat.shared.Strings.endingWith;

public abstract class JdbcRepository {

    protected abstract Connection getConnection();

    protected <R> Stream<R> read(ResultSet rset, UnsafeFunction<ResultSet, R> fn) {
        return Streams.generate(() -> {
            try {
                if (!rset.next()) return Optional.empty();
                return Optional.of(fn.apply(rset));
            } catch (Throwable t) {
                throw propagate(t);
            }
        });
    }

    protected <T> int execute(String query, T obj, BiConsumer<T, PreparedStatement> fn) {
        try (Connection co = getConnection();
             PreparedStatement s = co.prepareStatement(query))
        {
            fn.accept(obj, s);
            return s.executeUpdate();
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    protected int execute(String query, UnsafeConsumer<PreparedStatement> fn) {
        try (Connection co = getConnection();
             PreparedStatement s = co.prepareStatement(query))
        {
            fn.accept(s);
            return s.executeUpdate();
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    @SneakyThrows
    protected <R> Stream<R> query(String query, UnsafeConsumer<PreparedStatement> params, UnsafeFunction<ResultSet, R> fn) {
        Connection co = getConnection();
        PreparedStatement s = co.prepareStatement(query);
        params.accept(s);
        ResultSet rset = s.executeQuery();

        return read(rset, fn)
                .onClose(() -> {
                    try {rset.close();} catch (SQLException ignored) {}
                    try {s.close();} catch (SQLException ignored) {}
                    try {co.close();} catch (SQLException ignored) {}
                });
    }

    @SneakyThrows
    protected <R> Stream<R> query(String query, UnsafeFunction<ResultSet, R> fn) {
        Connection co = getConnection();
        Statement s = co.createStatement();
        ResultSet rset = s.executeQuery(query);
        return read(rset, fn)
                .onClose(() -> {
                    try {rset.close();} catch (SQLException ignored) {}
                    try {s.close();} catch (SQLException ignored) {}
                    try {co.close();} catch (SQLException ignored) {}
                });
    }

    @SneakyThrows
    protected <R> R unique(ResultSet rset, UnsafeFunction<ResultSet, R> fn) {
        if (!rset.next()) throw new NoSuchElementException();
        R res = fn.apply(rset);
        if (rset.next()) throw new NonUniqueElementException();
        return res;
    }

    @SneakyThrows
    protected <R> Optional<R> uniqueOpt(ResultSet rset, UnsafeFunction<ResultSet, R> fn) {
        if (!rset.next()) return Optional.empty();
        Optional<R> res = Optional.of(fn.apply(rset));
        if (rset.next()) throw new NonUniqueElementException();
        return res;
    }

    @SneakyThrows
    protected int uniqueInt(ResultSet rset) {
        if (!rset.next()) throw new NoSuchElementException();
        int res = rset.getInt(1);
        if (rset.next()) throw new NonUniqueElementException();
        return res;
    }

    @SneakyThrows
    protected long uniqueLong(ResultSet rset) {
        if (!rset.next()) throw new NoSuchElementException();
        long res = rset.getLong(1);
        if (rset.next()) throw new NonUniqueElementException();
        return res;
    }

    @SneakyThrows
    protected int[] asIntArray(Array array) {
        try {
            Integer[] arr = (Integer[]) array.getArray();
            int[] res = new int[arr.length];
            for (int i = 0; i < arr.length; i++) {
                res[i] = arr[i];
            }
            return res;
        } finally {
            array.free();
        }
    }

    protected String simpleSelect(String table, Collection<String> fields) {
        return "select " + fields.stream().collect(joining(", ")) +
                " from " + table
                ;
    }

    protected String simpleSelect(String table, String key, Collection<String> fields) {
        return simpleSelect(table, fields) + " where " + key + "=?";
    }

    protected String simpleInsert(String table, Collection<String> fields) {
        return "insert into " + table + "(" +
                fields.stream().collect(joining(", ")) +
                ") values(" + fields.stream().map(i -> "?").collect(joining(", ")) +
                ")"
                ;
    }

    protected String simpleInsert(String table, String id, Collection<String> fields) {
        return simpleInsert(table, fields) + " returning " + id;
    }

    protected String simpleUpdate(String table, String id, Collection<String> fields) {
        return "update " + table + " set " +
                fields.stream().filter(x -> !x.equals(id))
                               .map(endingWith("=?"))
                               .collect(joining(", ")) +
                " where " + id + "=?"
                ;
    }

    protected String simpleDelete(String table, String id) {
        return "delete from " + table + " where " + id + "=?";
    }
}

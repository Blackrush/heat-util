package org.heat.shared.database;

import lombok.SneakyThrows;
import org.fungsi.concurrent.Future;
import org.fungsi.function.UnsafeFunction;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.heat.shared.function.UnsafeFunctions.UnsafeBiConsumer;

public final class TableBuilder<T> {

    private String tableName = null;
    private List<String> primaryKeys = null;
    private List<String> selectableColumns = null;
    private List<String> insertableColumns = null;
    private List<String> updatableColumns = null;
    private UnsafeBiConsumer<NamedPreparedStatement, T> setPrimaryKeys = null;
    private UnsafeFunction<ResultSet, Future<T>> importFromDb = null;
    private UnsafeBiConsumer<NamedPreparedStatement, T> insertToDb = null;
    private UnsafeBiConsumer<NamedPreparedStatement, T> updateToDb = null;
    
    public TableBuilder<T> setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }
    
    public TableBuilder<T> setPrimaryKeys(List<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
        return this;
    }

    public TableBuilder<T> setSinglePrimaryKey(String primaryKey) {
        this.primaryKeys = Arrays.asList(primaryKey);
        return this;
    }
    
    public TableBuilder<T> setAllColumns(List<String> allColumns) {
        this.selectableColumns = allColumns;
        this.insertableColumns = allColumns;
        this.updatableColumns = allColumns;
        return this;
    }

    public TableBuilder<T> setSelectableColumns(List<String> selectableColumns) {
        this.selectableColumns = selectableColumns;
        return this;
    }
    
    public TableBuilder<T> setInsertableColumns(List<String> insertableColumns) {
        this.insertableColumns = insertableColumns;
        return this;
    }
    
    public TableBuilder<T> setUpdatableColumns(List<String> updatableColumns) {
        this.updatableColumns = updatableColumns;
        return this;
    }
    
    public TableBuilder<T> doSetPrimaryKeys(UnsafeBiConsumer<NamedPreparedStatement, T> setPrimaryKeys) {
        this.setPrimaryKeys = setPrimaryKeys;
        return this;
    }
    
    public TableBuilder<T> doImportFromDb(UnsafeFunction<ResultSet, Future<T>> importFromDb) {
        this.importFromDb = importFromDb;
        return this;
    }
    
    public TableBuilder<T> doInsertToDb(UnsafeBiConsumer<NamedPreparedStatement, T> insertToDb) {
        this.insertToDb = insertToDb;
        return this;
    }

    public TableBuilder<T> doUpdateToDb(UnsafeBiConsumer<NamedPreparedStatement, T> updateToDb) {
        this.updateToDb = updateToDb;
        return this;
    }

    public TableBuilder<T> doExportToDb(UnsafeBiConsumer<NamedPreparedStatement, T> fn) {
        this.insertToDb = fn;
        this.updateToDb = fn;
        return this;
    }
    
    public Table<T> build() {
        requireNonNull(tableName, "tableName");
        requireNonNull(primaryKeys, "primaryKeys");
        requireNonNull(insertableColumns, "insertableColumns");
        requireNonNull(updatableColumns, "updatableColumns");
        requireNonNull(setPrimaryKeys, "setPrimaryKeys");
        requireNonNull(importFromDb, "importFromDb");
        requireNonNull(insertToDb, "insertToDb");
        requireNonNull(updateToDb, "updateToDb");

        return new Table<T>() {
            @Override
            public String getTableName() {
                return tableName;
            }

            @Override
            public List<String> getPrimaryKeys() {
                return primaryKeys;
            }

            @Override
            public List<String> getSelectableColumns() {
                return selectableColumns;
            }

            @Override
            public List<String> getInsertableColumns() {
                return insertableColumns;
            }

            @Override
            public List<String> getUpdatableColumns() {
                return updatableColumns;
            }

            @SneakyThrows
            @Override
            public void setPrimaryKeys(NamedPreparedStatement s, T val) {
                setPrimaryKeys.accept(s, val);
            }

            @SneakyThrows
            @Override
            public Future<T> importFromDb(ResultSet rset) {
                return importFromDb.apply(rset);
            }

            @SneakyThrows
            @Override
            public void insertToDb(NamedPreparedStatement s, T val) {
                insertToDb.accept(s, val);
            }

            @SneakyThrows
            @Override
            public void updateToDb(NamedPreparedStatement s, T val) {
                updateToDb.accept(s, val);
            }
        };
    }
}

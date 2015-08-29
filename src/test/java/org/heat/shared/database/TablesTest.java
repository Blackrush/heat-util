package org.heat.shared.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.fungsi.concurrent.Futures;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.collect.ImmutableList.of;
import static org.junit.Assert.assertEquals;

public class TablesTest {

    private Table table;

    @Data
    @AllArgsConstructor
    private static class User {
        int id;
        String username;
        String password;
    }

    @Before
    public void setUp() throws Exception {
        table = Table.<User>builder()
                .setTableName("users")
                .setSinglePrimaryKey("id")
                .setAllColumns(of("username", "password"))
                .doSetPrimaryKeys((s, user) ->
                    s.setInt("id", user.getId())
                )
                .doImportFromDb(rset -> Futures.success(new User(
                        rset.getInt("id"),
                        rset.getString("username"),
                        rset.getString("password")
                )))
                .doExportToDb((s, user) -> {
                    s.setString("username", user.getUsername());
                    s.setString("password", user.getPassword());
                })
                .build();
    }

    @Test
    public void testCreateInsertQuery() throws Exception {
        // given

        // when
        String sql = Tables.createInsertQuery(table);

        // then
        assertEquals("sql", "insert into users(id, username, password) values(:id, :username, :password)", sql);
    }

    @Test
    public void testCreateUpdateQuery() throws Exception {
        // given

        // when
        String sql = Tables.createUpdateQuery(table);

        // then
        assertEquals("sql", "update users set username=:username, password=:password where id=:id", sql);
    }

    @Test
    public void testCreateDeleteQuery() throws Exception {
        // given

        // when
        String sql = Tables.createDeleteQuery(table);

        // then
        assertEquals("sql", "delete from users where id=:id", sql);
    }

    @Test
    public void testCreateSelectQuery() throws Exception {
        // given

        // when
        String sql = Tables.createSelectQuery(table);

        // then
        assertEquals("sql", "select id, username, password from users", sql);
    }
}
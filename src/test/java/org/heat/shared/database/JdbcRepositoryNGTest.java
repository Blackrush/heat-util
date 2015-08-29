package org.heat.shared.database;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.fungsi.concurrent.Futures;
import org.fungsi.concurrent.Worker;
import org.fungsi.concurrent.Workers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class JdbcRepositoryNGTest {

    @Data
    @AllArgsConstructor
    private static class User {
        int id;
        String foobar;
    }

    private static class UserRepository extends JdbcRepositoryNG<User> {
        private final Connection connection;

        public UserRepository(Table<User> table, Worker worker, Connection connection) {
            super(table, worker);
            this.connection = connection;
        }

        @Override
        protected Connection getConnection() {
            return connection;
        }
    }

    private Connection connection;
    private UserRepository repository;

    @Before
    public void setUp() throws Exception {
        connection = mock(Connection.class);

        Table<User> userTable = Table.<User>builder()
                .setTableName("users")
                .setSinglePrimaryKey("id")
                .setAllColumns(ImmutableList.of("foobar"))
                .doImportFromDb(rset -> Futures.success(new User(rset.getInt("id"), rset.getString("foobar"))))
                .doSetPrimaryKeys((s, user) -> s.setInt("id", user.getId()))
                .doExportToDb((s, user) -> s.setString("foobar", user.getFoobar()))
                .build();

        repository = new UserRepository(userTable, Workers.wrap(MoreExecutors.sameThreadExecutor()), connection);
    }

    @Test
    public void testInsert() throws Exception {
        // given
        User user = new User(1, "buzz");

        PreparedStatement statement = mock(PreparedStatement.class);
        InOrder inOrder = inOrder(connection, statement);

        // when
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.insert(user).get();

        // then
        inOrder.verify(connection).prepareStatement("insert into users(id, foobar) values(?, ?)");
        inOrder.verify(statement).setInt(1, 1);
        inOrder.verify(statement).setString(2, "buzz");
        inOrder.verify(statement).execute();
        inOrder.verify(statement).close();
        inOrder.verify(connection).close();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testUpdate() throws Exception {
        // given
        User user = new User(1, "buzz");

        PreparedStatement statement = mock(PreparedStatement.class);
        InOrder inOrder = inOrder(connection, statement);

        // when
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.update(user).get();

        // then
        inOrder.verify(connection).prepareStatement("update users set foobar=? where id=?");
        inOrder.verify(statement).setInt(2, 1);
        inOrder.verify(statement).setString(1, "buzz");
        inOrder.verify(statement).execute();
        inOrder.verify(statement).close();
        inOrder.verify(connection).close();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testDelete() throws Exception {
        // given
        User user = new User(1, "buzz");

        PreparedStatement statement = mock(PreparedStatement.class);
        InOrder inOrder = inOrder(connection, statement);

        // when
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.delete(user).get();

        // then
        inOrder.verify(connection).prepareStatement("delete from users where id=?");
        inOrder.verify(statement).setInt(1, 1);
        inOrder.verify(statement).execute();
        inOrder.verify(statement).close();
        inOrder.verify(connection).close();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testFindFirst() throws Exception {
        // given
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        InOrder inOrder = inOrder(connection, statement, resultSet);

        // when
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("foobar")).thenReturn("buzz");

        User user = repository.findFirst("select id, foobar from users where id=:id", s -> s.setInt("id", 1)).get();

        // then
        assertThat("user id", user.getId(), equalTo(1));
        assertThat("user foobar", user.getFoobar(), equalTo("buzz"));

        inOrder.verify(connection).prepareStatement("select id, foobar from users where id=?");
        inOrder.verify(statement).setInt(1, 1);
        inOrder.verify(statement).executeQuery();
        inOrder.verify(resultSet).next();
        inOrder.verify(resultSet).getInt("id");
        inOrder.verify(resultSet).getString("foobar");
        inOrder.verify(resultSet).close();
        inOrder.verify(statement).close();
        inOrder.verify(connection).close();
    }

    @Test
    public void testFindList() throws Exception {
        // given
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        InOrder inOrder = inOrder(connection, statement, resultSet);

        // when
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("foobar")).thenReturn("buzz");

        List<User> users = repository.findList("select id, foobar from users where foobar=:foobar", s -> s.setString("foobar", "buzz")).get();

        // then
        assertThat("number of users", users.size(), equalTo(1));
        assertThat("users", users, hasItem(new User(1, "buzz")));

        inOrder.verify(connection).prepareStatement("select id, foobar from users where foobar=?");
        inOrder.verify(statement).setString(1, "buzz");
        inOrder.verify(statement).executeQuery();
        inOrder.verify(resultSet).next();
        inOrder.verify(resultSet).getInt("id");
        inOrder.verify(resultSet).getString("foobar");
        inOrder.verify(resultSet).close();
        inOrder.verify(statement).close();
        inOrder.verify(connection).close();
    }
}
package org.heat.shared.database;

import com.google.common.collect.Multimap;
import org.heat.shared.Pair;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class NamedPreparedStatementImplTest {

    @Test
    public void testParse_SinglePlaceholders() throws Exception {
        // given
        String sql = "insert into users(id, name, age) values(:id, :name, :age)";

        // when
        Pair<String, Multimap<String, Integer>> pair = NamedPreparedStatementImpl.parse(sql);
        String parsed = pair.first;
        Multimap<String, Integer> indexes = pair.second;

        // then
        assertEquals("parsed query", "insert into users(id, name, age) values(?, ?, ?)", parsed);
        assertThat("number of indexes", indexes.size(), equalTo(3));
        assertThat("id index", indexes.get("id"), hasItem(1));
        assertThat("name index", indexes.get("name"), hasItem(2));
        assertThat("age index", indexes.get("age"), hasItem(3));
    }

    @Test
    public void testParse_MultiplePlaceholders() throws Exception {
        // given
        String sql = "insert into foo(bar, baz) values(:buzz, :buzz)";

        // when
        Pair<String, Multimap<String, Integer>> pair = NamedPreparedStatementImpl.parse(sql);
        String parsed = pair.first;
        Multimap<String, Integer> indexes = pair.second;

        // then
        assertEquals("parsed query", "insert into foo(bar, baz) values(?, ?)", parsed);
        assertThat("number of indexes", indexes.size(), equalTo(2));
        assertThat("buzz index", indexes.get("buzz"), hasItem(1));
        assertThat("buzz index", indexes.get("buzz"), hasItem(2));
    }

    @Test
    public void testParse_EndingPlaceholder() throws Exception {
        // given
        String sql = "select foo from baz where buzz=:qux";

        // when
        Pair<String, Multimap<String, Integer>> pair = NamedPreparedStatementImpl.parse(sql);
        String parsed = pair.first;
        Multimap<String, Integer> indexes = pair.second;

        // then
        assertEquals("parsed query", "select foo from baz where buzz=?", parsed);
        assertThat("number of indexes", indexes.size(), equalTo(1));
        assertThat("qux index", indexes.get("qux"), hasItem(1));
    }
}
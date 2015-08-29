package org.heat.shared;

import org.fungsi.concurrent.Future;
import org.fungsi.concurrent.Promise;
import org.fungsi.concurrent.Promises;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoreFuturesTest {

    @Test
    public void testJoin() throws Exception {
        // given
        Promise<String> left = Promises.create();
        Promise<Integer> right = Promises.create();

        // when
        Future<Pair<String, Integer>> future = MoreFutures.join(left, right);

        // then
        assertFalse("future is done", future.isDone());

        left.complete("lol");
        assertFalse("future is done", future.isDone());

        right.complete(42);
        assertTrue("future is done", future.isDone());

        assertEquals("future result", future.get(), Pair.of("lol", 42));
    }
}
package org.heat.guicy;

import com.google.inject.*;
import lombok.NonNull;
import lombok.Value;
import org.fungsi.concurrent.Worker;
import org.fungsi.concurrent.Workers;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertTrue;

public class ContextualScopeImplTest {
    @Retention(RetentionPolicy.RUNTIME)
    @ScopeAnnotation
    public static @interface MyScope { }

    @Value
    public static class MyCtx {
        @NonNull final String value;
    }

    @MyScope
    public static class MyService {
        @Inject MyCtx ctx;
    }

    Worker worker;
    ContextualScopeImpl<MyCtx> scope;
    Provider<MyService> myService;

    @Before
    public void setUp() throws Exception {
        worker = Workers.wrap(Executors.newSingleThreadExecutor());

        scope = new ContextualScopeImpl<>(MyScope.class, Key.get(MyCtx.class));

        Injector injector = Guice.createInjector(scope);
        myService = injector.getProvider(MyService.class);
    }

    @Test
    public void testIdentity() throws Exception {
        // given
        MyCtx ctx = new MyCtx("LOL");
        MyService first, second;

        // when
        scope.enter(ctx);
        try {
            first = myService.get();
        } finally {
            scope.quit();
        }

        scope.enter(ctx);
        try {
            second = myService.get();
        } finally {
            scope.quit();
        }

        // then
        assertTrue("provider should give same instance everytime", first == second);
    }
}
package org.heat.shared;

import com.google.common.collect.ImmutableList;
import org.fungsi.Unit;
import org.fungsi.concurrent.Future;
import org.fungsi.concurrent.Futures;
import org.fungsi.concurrent.Promise;
import org.fungsi.concurrent.Promises;
import org.fungsi.function.UnsafeConsumer;
import org.fungsi.function.UnsafeFunction;
import org.fungsi.function.UnsafeRunnable;
import org.fungsi.function.UnsafeSupplier;
import org.heat.shared.stream.ImmutableCollectors;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class MoreFutures {
    private MoreFutures() {}

    public static <T> Future<T> apply(UnsafeSupplier<T> fn) {
        return Future.constant(fn.safelyGet());
    }

    public static Future<Unit> run(UnsafeRunnable fn) {
        return Future.constant(fn.safelyRun());
    }

    public static <T> UnsafeFunction<T, Future<Unit>> wrap(UnsafeConsumer<T> fn) {
        return param -> {
            fn.accept(param);
            return Futures.unit();
        };
    }

    public static <T> Future<ImmutableList<T>> collect(final List<Future<T>> futures) {
        if (futures.isEmpty()) {
            return Futures.success(ImmutableList.of());
        }
        if (futures.size() == 1) {
            return futures.get(0).map(ImmutableList::of);
        }

        final Promise<ImmutableList<T>> promise = Promises.create();
        final Object lock = new Object();
        final int[] countDown = new int[] {futures.size()};
        final ImmutableList.Builder<T> builder = ImmutableList.builder();

        for (Future<T> future : futures) {
            future
                .onFailure(promise::fail)
                .onSuccess(res -> {
                    if (promise.isDone()) return;

                    synchronized (lock) {
                        builder.add(res);
                        countDown[0]--;

                        if (countDown[0] <= 0) {
                            promise.complete(builder.build());
                        }
                    }
                })
                ;
        }

        return promise;
    }

    public static <T> Collector<Future<T>, ?, Future<ImmutableList<T>>> collect() {
        return Collectors.collectingAndThen(
                ImmutableCollectors.toList(),
                MoreFutures::collect
        );
    }

    public static <A, B> Future<Pair<A, B>> collectPair(Pair<Future<A>, Future<B>> pair) {
        return join(pair.first, pair.second);
    }

    @SuppressWarnings("unchecked")
    public static <A, B> Future<Pair<A, B>> join(Future<A> a, Future<B> b) {
        final Promise<Pair<A, B>> promise = Promises.create();

        final Object[] buffered = new Object[]{null};

        a.respond(e ->
            e.ifLeft(val -> {
                synchronized (buffered) {
                    if (buffered[0] == null) {
                        buffered[0] = val;
                    } else {
                        promise.complete(Pair.of(val, (B) buffered[0]));
                    }
                }
            }).ifRight(err -> {
                if (!promise.isDone()) {
                    promise.fail(err);
                }
            })
        );

        b.respond(e ->
            e.ifLeft(val -> {
                synchronized (buffered) {
                    if (buffered[0] == null) {
                        buffered[0] = val;
                    } else {
                        promise.complete(Pair.of((A) buffered[0], val));
                    }
                }
            }).ifRight(err -> {
                if (!promise.isDone()) {
                    promise.fail(err);
                }
            })
        );

        return promise;
    }
}

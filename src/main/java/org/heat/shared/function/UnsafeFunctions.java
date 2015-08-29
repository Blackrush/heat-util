package org.heat.shared.function;

import org.fungsi.Either;
import org.fungsi.Unit;
import org.fungsi.function.UnsafeFunction;
import org.fungsi.function.UnsafeRunnable;
import org.fungsi.function.UnsafeSupplier;

import java.util.Optional;
import java.util.function.*;

import static com.google.common.base.Throwables.propagate;

public final class UnsafeFunctions {
    @FunctionalInterface
    public static interface UnsafeConsumer<T> {
        void accept(T t) throws Throwable;
    }

    @FunctionalInterface
    public static interface UnsafeBiFunction<T, U, R> {
        R apply(T t, U u) throws Throwable;
    }

    @FunctionalInterface
    public static interface UnsafeBiConsumer<T, U> {
        void accept(T t, U u) throws Throwable;
    }

    public static <T> T retrow(UnsafeSupplier<T> fn) {
        try {
            return fn.get();
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    public static void retrowTask(UnsafeRunnable fn) {
        try {
            fn.run();
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    public static <T, R> Function<T, R> unsafe(UnsafeFunction<T, R> fn) {
        return fn.unsafeFunction();
    }

    public static <T, R> Function<T, R> fail(Supplier<Throwable> fn) {
        return param -> {
            throw propagate(fn.get());
        };
    }

    public static <T extends Throwable, R> Function<T, R> rethrow() {
        return param -> {
            throw propagate(param);
        };
    }

    public static Runnable unsafeTask(UnsafeRunnable task) {
        return task.unsafeRunnable();
    }

    public static <T> Consumer<T> unsafeConsumer(UnsafeConsumer<T> fn) {
        return param -> {
            try {
                fn.accept(param);
            } catch (Throwable e) {
                throw propagate(e);
            }
        };
    }

	public static <T> Optional<T> compute(UnsafeSupplier<T> fn) {
		try {
			return Optional.of(fn.get());
		} catch (Throwable t) {
			return Optional.empty();
		}
	}

    public static <T> Either<T, Throwable> safeCompute(UnsafeSupplier<T> fn) {
        try {
            return Either.success(fn.get());
        } catch (Throwable cause) {
            return Either.failure(cause);
        }
    }

    public static <T> Supplier<T> unsafeSupplier(UnsafeSupplier<T> fn) {
        return fn.unsafeSupplier();
    }

    public static <T> Supplier<Either<T, Throwable>> safeSupplier(UnsafeSupplier<T> fn) {
        return fn.safeSupplier();
    }

    public static <T, R> Function<T, Either<R, Throwable>> safeFunction(org.fungsi.function.UnsafeFunction<T, R> fn) {
        return fn.safeFunction();
    }

    public static <T, U, R> BiFunction<T, U, Either<R, Throwable>> safeBiFunction(UnsafeBiFunction<T, U, R> fn) {
        return (a, b) -> {
            try {
                return Either.success(fn.apply(a, b));
            } catch (Throwable cause) {
                return Either.failure(cause);
            }
        };
    }

    public static <T, U> BiConsumer<T, U> unsafeBiConsumer(UnsafeBiConsumer<T, U> fn) {
        return (a, b) -> {
            try {
                fn.accept(a, b);
            } catch (Throwable cause) {
                throw propagate(cause);
            }
        };
    }

    public static <T, U> BiFunction<T, U, Either<Unit, Throwable>> safeBiConsumer(UnsafeBiConsumer<T, U> fn) {
        return (a, b) -> {
            try {
                fn.accept(a, b);
                return Unit.left();
            } catch (Throwable cause) {
                return Either.failure(cause);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> Function<Object, T> cast() {
        return o -> (T) o;
    }

    public static <T> Either<T, Throwable> apply(UnsafeSupplier<T> fn) {
        return fn.safelyGet();
    }

    public static Either<Unit, Throwable> apply(UnsafeRunnable fn) {
        return fn.safelyRun();
    }
}

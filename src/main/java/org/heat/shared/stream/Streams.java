package org.heat.shared.stream;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Streams {
	private Streams() {}

    public static <T> Stream<T> takeWhile(BooleanSupplier fn, Supplier<T> fnVal) {
        return StreamSupport.stream(new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, 0) {
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                if (!fn.getAsBoolean()) return false;
                action.accept(fnVal.get());
                return true;
            }
        }, false);
    }

	/**
	 * generates values until an empty value is given
	 * @param seed the first value
	 * @param fn the bind function
	 * @param <T> the value type
	 * @return a lazy {@link java.util.stream.Stream}
	 */
	public static <T> Stream<T> iterate(Optional<T> seed, Function<T, Optional<T>> fn) {
		return StreamSupport.stream(new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, 0) {
			Optional<T> current = seed;

			@Override
			public boolean tryAdvance(Consumer<? super T> action) {
				current.ifPresent(action);
				current = current.flatMap(fn);
				return current.isPresent();
			}
		}, false);
	}

	/**
	 * @see Streams#iterate(java.util.Optional, java.util.function.Function)
	 */
	public static <T> Stream<T> iterate(T seed, Function<T, Optional<T>> fn) {
		return iterate(Optional.ofNullable(seed), fn);
	}

    public static IntStream iterateInt(int seed, IntFunction<OptionalInt> fn) {
        return StreamSupport.intStream(new Spliterators.AbstractIntSpliterator(Long.MAX_VALUE, 0) {
            OptionalInt cur = OptionalInt.of(seed);

            @Override
            public boolean tryAdvance(IntConsumer action) {
                cur.ifPresent(action);
                cur = flatMap(cur, fn);
                return cur.isPresent();
            }
        }, false);
    }

    public static OptionalInt flatMap(OptionalInt self, IntFunction<OptionalInt> fn) {
        if (!self.isPresent()) {
            return self;
        }
        return fn.apply(self.getAsInt());
    }

    /**
     * generate an integer stream until an empty value is given
     * @param fn the bind function
     * @return a lazy {@link java.util.stream.Stream}
     */
    public static IntStream generateInts(Supplier<OptionalInt> fn) {
        return StreamSupport.intStream(new Spliterators.AbstractIntSpliterator(Long.MAX_VALUE, 0) {@Override
            public boolean tryAdvance(IntConsumer action) {
                OptionalInt opt = fn.get();
                opt.ifPresent(action);
                return opt.isPresent();
            }
        }, false);
    }

    public static <T> Stream<T> generate(Supplier<Optional<T>> fn) {
        return StreamSupport.stream(new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, 0) {
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                Optional<T> opt = fn.get();
                opt.ifPresent(action);
                return opt.isPresent();
            }
        }, false);
    }

	public static LongStream times(long n) {
		return LongStream.range(0L, n);
	}

	public static IntStream times(int n) {
		return IntStream.range(0, n);
	}

    public static IntStream times(int start, int end, int step) {
        return iterateInt(start, it -> {
            if (it >= end) return OptionalInt.empty();
            return OptionalInt.of(it + step);
        });
    }

    public static <T> Function<Optional<T>, Stream<T>> flattenOption() {
        return x -> x.isPresent() ? Stream.of(x.get()) : Stream.empty();
    }

    public static <T> Stream<T> alternate(Spliterator<T> left, Spliterator<T> right) {
        return StreamSupport.stream(new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, 0) {
            boolean flag = true;
            Spliterator<T> last;

            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                if (last != null) {
                    return last.tryAdvance(action);
                }

                if (flag) {
                    if (!left.tryAdvance(action)) {
                        last = right;
                        return last.tryAdvance(action);
                    } else {
                        flag = !flag;
                        return true;
                    }
                } else {
                    if (!right.tryAdvance(action)) {
                        last = left;
                        return last.tryAdvance(action);
                    } else {
                        flag = !flag;
                        return true;
                    }
                }
            }
        }, false);
    }
}

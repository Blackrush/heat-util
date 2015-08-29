package org.heat.shared.stream;

import org.fungsi.Either;
import org.fungsi.Unit;
import org.heat.shared.NonUniqueElementException;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class MoreCollectors {

    public static final Collector<Object, AtomicReference<Object>, Optional<Object>> UNIQUE_OPTION_COLLECTOR = new Collector<Object, AtomicReference<Object>, Optional<Object>>() {
        @Override
        public Supplier<AtomicReference<Object>> supplier() {
            return AtomicReference::new;
        }

        @Override
        public BiConsumer<AtomicReference<Object>, Object> accumulator() {
            return (ref, o) -> {
                if (ref.get() == null) {
                    ref.set(o);
                } else {
                    throw new NonUniqueElementException();
                }
            };
        }

        @Override
        public BinaryOperator<AtomicReference<Object>> combiner() {
            return (a, b) -> {
                throw new NonUniqueElementException();
            };
        }

        @Override
        public Function<AtomicReference<Object>, Optional<Object>> finisher() {
            return ref -> Optional.ofNullable(ref.get());
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.noneOf(Characteristics.class);
        }
    };

    private MoreCollectors() {}

    @SuppressWarnings("unchecked")
    public static <T> Collector<T, ?, Optional<T>> uniqueOption() {
        return (Collector) UNIQUE_OPTION_COLLECTOR;
    }

    public static <T> Collector<T, ?, T> unique() {
        return Collectors.collectingAndThen(uniqueOption(), Optional::get);
    }

    public static <T> Collector<T, ?, Either<T, Unit>> uniqueMaybe() {
        return new Collector<T, AtomicReference<T>, Either<T, Unit>>() {
            @Override
            public Supplier<AtomicReference<T>> supplier() {
                return AtomicReference::new;
            }

            @Override
            public BiConsumer<AtomicReference<T>, T> accumulator() {
                return (acc, x) -> {
                    if (acc.get() != null) {
                        throw new NonUniqueElementException();
                    }
                    acc.set(x);
                };
            }

            @Override
            public BinaryOperator<AtomicReference<T>> combiner() {
                return (a, b) -> {
                    if (a.get() != null && b.get() != null) {
                        throw new NonUniqueElementException();
                    }
                    if (a.get() != null) {
                        return a;
                    }
                    return b;
                };
            }

            @Override
            public Function<AtomicReference<T>, Either<T, Unit>> finisher() {
                return acc -> {
                    if (acc.get() != null) {
                        return Either.left(acc.get());
                    }
                    return Unit.right();
                };
            }

            @Override
            public Set<Characteristics> characteristics() {
                return EnumSet.noneOf(Characteristics.class);
            }
        };
    }
}

package org.heat.shared;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Wither;
import org.fungsi.concurrent.Future;

import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Wither
public final class Pair<T1, T2> implements Tuple {
    public final T1 first;
    public final T2 second;

    @SuppressWarnings("unchecked")
    public static <T1, T2> Pair<T1, T2> of(T1 first, T2 second) {
        return new Pair(first, second);
    }

    public Object at(int i) {
        switch (i) {
            case 0: return first;
            case 1: return second;
            default: throw new IndexOutOfBoundsException();
        }
    }

    @SuppressWarnings("unchecked")
    public Pair at(int i, Object o) {
        switch (i) {
            case 0: return new Pair(o, second);
            case 1: return new Pair(first, second);
            default: throw new IndexOutOfBoundsException();
        }
    }

    public Stream<Object> asStream() {
        return Stream.of(first, second);
    }

    public <R1, R2> Future<Pair<R1, R2>> compute(Function<T1, Future<R1>> firstFn, Function<T2, Future<R2>> secondFn) {
        return MoreFutures.join(firstFn.apply(first), secondFn.apply(second));
    }
}

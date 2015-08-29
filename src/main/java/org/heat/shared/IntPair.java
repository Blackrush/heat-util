package org.heat.shared;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Wither;

import java.util.stream.Stream;

@AllArgsConstructor(staticName = "of")
@ToString
@EqualsAndHashCode
@Wither
public final class IntPair implements Tuple {
    public final int first;
    public final int second;

    @Override
    public Object at(int i) {
        switch (i) {
            case 0: return first;
            case 1: return second;
            default: throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public IntPair at(int i, Object o) {
        switch (i) {
            case 0: return withFirst((int) o);
            case 1: return withSecond((int) o);
            default: throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public Stream<Object> asStream() {
        return Stream.of(first, second);
    }
}

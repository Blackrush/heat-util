package org.heat.shared;

import java.util.stream.Stream;

public interface Tuple {
    Object at(int i);
    Tuple at(int i, Object o);
    Stream<Object> asStream();
}

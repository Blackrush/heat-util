package org.heat.shared.function;

import java.util.function.Supplier;

public final class Functions {
	private Functions() {}

    private static final Runnable NOOP = () -> {};

    public static Runnable noop() {
        return NOOP;
    }

	public static <T> Supplier<T> supplier(T o) {
		return () -> o;
	}
}

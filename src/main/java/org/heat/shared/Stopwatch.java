package org.heat.shared;

import java.time.Clock;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public final class Stopwatch {
    @FunctionalInterface
    public static interface Chrono {
        long now();
    }

    @FunctionalInterface
    public static interface Calculator {
        Duration between(long start, long end);
    }

    private final Chrono chrono;
    private final Calculator calculator;
    private long start, end;

    private Stopwatch(Chrono chrono, Calculator calculator) {
        this.chrono = chrono;
        this.calculator = calculator;
    }

    public final class H implements AutoCloseable {
        private H() {
            start = chrono.now();
        }

        @Override
        public void close() {
            end = chrono.now();
        }
    }

    public H start() {
        return new H();
    }

    public Duration elapsed() {
        return calculator.between(start, end);
    }

    public static Stopwatch newStopwatch(Chrono chrono, Calculator calculator) {
        return new Stopwatch(chrono, calculator);
    }

    public static Stopwatch system() {
        return newStopwatch(System::nanoTime, (start, end) -> Duration.of(end - start, ChronoUnit.NANOS));
    }

    public static Stopwatch newStopwatch(Clock clock) {
        return newStopwatch(clock::millis, (start, end) -> Duration.of(end - start, ChronoUnit.MILLIS));
    }
}

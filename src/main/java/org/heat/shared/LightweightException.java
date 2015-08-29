package org.heat.shared;

public abstract class LightweightException extends RuntimeException {
    protected LightweightException(String message) {
        super(message);
    }

    protected LightweightException(String message, Throwable cause) {
        super(message, cause);
    }

    protected LightweightException(Throwable cause) {
        super(cause);
    }

    @Override
    public final Throwable fillInStackTrace() {
        return this;
    }
}

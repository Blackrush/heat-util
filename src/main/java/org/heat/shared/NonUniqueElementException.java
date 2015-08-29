package org.heat.shared;

public class NonUniqueElementException extends RuntimeException {
    public NonUniqueElementException() {
    }

    public NonUniqueElementException(String message) {
        super(message);
    }

    public NonUniqueElementException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonUniqueElementException(Throwable cause) {
        super(cause);
    }

    public NonUniqueElementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

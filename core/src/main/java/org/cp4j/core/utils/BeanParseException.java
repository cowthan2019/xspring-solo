package org.cp4j.core.utils;

public class BeanParseException extends RuntimeException {

    public BeanParseException() {
    }

    public BeanParseException(String message) {
        super(message);
    }

    public BeanParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanParseException(Throwable cause) {
        super(cause);
    }

    public BeanParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

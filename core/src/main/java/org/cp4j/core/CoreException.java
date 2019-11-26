package org.cp4j.core;

public class CoreException extends RuntimeException {

    public CoreException() {
    }

    public CoreException(String message) {
        super(message);
    }

    public CoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public CoreException(Throwable cause) {
        super(cause);
    }

    public CoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public static void raise(String message){
        throw new CoreException(message);
    }

    public static void raise(String message, Throwable cause){
        throw new CoreException(message, cause);
    }

    public static void raise(Throwable cause){
        throw new CoreException(cause);
    }
}

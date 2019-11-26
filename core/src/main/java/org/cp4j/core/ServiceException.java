package org.cp4j.core;

public class ServiceException extends RuntimeException {

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    private Integer code ;

    public ServiceException(int code , String message) {
        super(message);
        this.code = code ;
    }

    public ServiceException(int code , String message, Throwable t ) {
        super(message,t);
        this.code = code ;
    }

    public Integer getCode() {
        return code ;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public static void raise(String message){
        throw new ServiceException(message);
    }

    public static void raise(String message, Throwable cause){
        throw new ServiceException(message, cause);
    }

    public static void raise(Throwable cause){
        throw new ServiceException(cause);
    }
}

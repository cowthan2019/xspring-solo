package org.cp4j.core.response;

/**
 * 可接受的逻辑异常，用于请求体
 */
public class LogicException extends Exception{

    private int code;
    private String error;

    public LogicException(int code, String msg){
        super(msg);
        this.code = code;
        this.error = msg;
    }

    public int getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

    public static void raise(int code, String error) throws LogicException{
        throw new LogicException(code, error);
    }
}

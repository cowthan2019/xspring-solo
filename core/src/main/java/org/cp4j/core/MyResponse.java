package org.cp4j.core;


/**
 * MyResponse
 * 响应结构数据
 *
 * @author zhangxuan
 * @date 2018/6/1
 */
public class MyResponse {

    /**
     * 请求结果
     */
    private int code;

    /**
     * 错误描述
     */
    private String message;

    /**
     * 返回数据
     */
    private Object data;

    public static MyResponse ok(Object result) {
        MyResponse response = new MyResponse();
        response.code = 0;
        response.message = null;
        response.data = result;
        return response;
    }

    public static MyResponse ok() {
        return ok(null);
    }


    public static MyResponse error(int code, String message) {
        MyResponse response = new MyResponse();
        response.code = code;
        response.message = message;
        response.data = null;
        return response;
    }

    public static MyResponse empty(){
        return ok(null);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

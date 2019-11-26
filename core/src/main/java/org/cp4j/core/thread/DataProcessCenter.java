package org.cp4j.core.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程下的请求和响应都放到这里处理，数据统一处理
 */
public abstract class DataProcessCenter {

    public static final class Data{
        public boolean isOut = false;
        public String data;
        public Exception error;
    }

    private final ExecutorService pool = Executors.newSingleThreadExecutor();

    private final List<Data> list = new ArrayList<>();

    public void addParam(long id, String request){
        Data data = new Data();
        data.isOut = false;
        data.data = request;
        list.add(data);
        pool.execute(new Runnable() {
            @Override
            public void run() {
                processData(data);
            }
        });
    }

    public void addResult(long id, String response, Exception exception){
        Data data = new Data();
        data.isOut = true;
        data.data = response;
        data.error = exception;
        list.add(data);
        pool.execute(new Runnable() {
            @Override
            public void run() {
                processData(data);
            }
        });
    }

    public abstract void processData(Data data);
}

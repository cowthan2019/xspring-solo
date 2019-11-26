package org.cp4j.core.http;


import org.cp4j.core.http.callback.ProgressRequestListener;
import org.cp4j.core.http.callback.ProgressResponseListener;
import org.cp4j.core.http.utils.HttpHelper;

import java.io.File;

/**
 * Created by cowthan on 2019/3/12.
 */

public class HttpEmitter {

    public long connectionTimeout = 30000;
    public long writeTimeout = 30000;
    public long readTimeout = 30000;

    public HttpWorker worker;

    public static HttpEmitter newEmitter(){
        return new HttpEmitter();
    }

    public HttpEmitter connectionTimeout(long time){
        connectionTimeout = time;
        return this;
    }

    public HttpEmitter writeTimeout(long time){
        writeTimeout = time;
        return this;
    }

    public HttpEmitter readTimeout(long time){
        readTimeout = time;
        return this;
    }


    public HttpEmitter worker(HttpWorker worker){
        this.worker = worker;
        return this;
    }

    public AyoResponse download(String url, File outFile, ProgressResponseListener progressResponseListener){
        if(outFile != null && outFile.exists()){
//            AyoResponse r = new AyoResponse();
//            r.data = outFile.getAbsolutePath();
//            r.failInfo = null;
//            return r;
            outFile.delete();
        }
        return worker.download(url, outFile, progressResponseListener);
    }


    public void fire(AyoRequest request){
        fire(request, null, null);
    }

    public void fire(AyoRequest request, ProgressRequestListener progressRequestListener, ProgressResponseListener progressResponseListener){

        if(request.pathParams.size() > 0){
            for(String key: request.pathParams.keySet()){
                request.url = request.url.replace("{" + key + "}", request.pathParams.get(key) + "");
            }
        }

        request.url = HttpHelper.makeURL(request.url, request.queryStrings);
        worker.fire(request, request.callback, progressRequestListener, progressResponseListener);
    }

    public AyoResponse fireSync(AyoRequest request){
        AyoResponse response = fireSync(request, null, null);
        return response;
    }

    public AyoResponse fireSync(AyoRequest request, ProgressRequestListener progressRequestListener, ProgressResponseListener progressResponseListener){
        if(request.pathParams.size() > 0){
            for(String key: request.pathParams.keySet()){
                request.url = request.url.replace("{" + key + "}", request.pathParams.get(key) + "");
            }
        }

        request.url = HttpHelper.makeURL(request.url, request.queryStrings);

        AyoResponse response = worker.fireSync(request, progressRequestListener, progressResponseListener);
        request.response = response;
        return response;
    }

    public void cancelAll(Object tag){
        worker.cancelAll(tag);
    }
}

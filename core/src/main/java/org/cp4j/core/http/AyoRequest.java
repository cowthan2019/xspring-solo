package org.cp4j.core.http;


import org.cp4j.core.AssocArray;
import org.cp4j.core.http.callback.BaseHttpCallback;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class AyoRequest {

    private AyoRequest(){
    }

    public static AyoRequest request(){
        AyoRequest r = new AyoRequest();
        return r;
    }

    public long id;

    public long startTime;

    public long endTime;

    public int requestCount;

    public Map<String, String> params = new HashMap<>();
    public Map<String, String> pathParams = new HashMap<>();
    public Map<String, String> queryStrings = new HashMap<>();

    public Map<String, String> headers = new HashMap<>();
    public Map<String, String> files = new HashMap<>();

    public String stringEntity;
    public String mediaType;

    public String file;

    public String url = "";
    public String method = "get";
    public String tag = "";

//    public StreamConverter<?> streamConverter;
//    public TopLevelConverter topLevelConverter;
//    public ResponseConverter resonseConverter;
    public BaseHttpCallback<String> callback;

    public boolean isDnsOn = false;

    public AyoRequest dnsEnable(boolean enable){
        isDnsOn = enable;
        return this;
    }

    //---------------------------------------------------------------//
    public AyoRequest tag(String tag){
        this.tag = tag;
        return this;
    }

    ///---------------
    public AyoRequest param(String name, String value){
        if(value == null) value = "";
        params.put(name, value);
        return this;
    }

    public AyoRequest params(AssocArray data){
        for (String name: data.keySet()){
            Object value = data.get(name);
            if(value == null) value = "";
            params.put(name, value + "");
        }
        return this;
    }

    public AyoRequest queryString(String name, String value){
        if(value == null) value = "";
        queryStrings.put(name, value);
        return this;
    }

    public AyoRequest path(String name, String value){
        if(value == null) value = "";
        pathParams.put(name, value);
        return this;
    }

    //--------------

    public AyoRequest paramFile(String name, String filePath){
        if(this.files == null) this.files = new HashMap<>();
        files.put(name, filePath);
        return this;
    }

    public AyoRequest file(String filePath){
        file = filePath;
        return this;
    }

    public AyoRequest mediaType(String mediaType){
        this.mediaType = mediaType;
        return this;
    }

    public AyoRequest mediaType(String mime, String charset){
        this.mediaType = mime + "; charset=" + charset;
        return this;
    }

    public AyoRequest header(String name, String value){
        if(this.headers == null) this.headers = new HashMap<String, String>();
        headers.put(name, value);
        return this;
    }

    public AyoRequest actionGet(){
        this.method = "get";
        return this;
    }

    public AyoRequest actionPost(){
        this.method = "post";
        return this;
    }

    public AyoRequest actionPut(){
        this.method = "put";
        return this;
    }

    public AyoRequest actionDelete(){
        this.method = "delete";
        return this;
    }

    public AyoRequest actionHead(){
        this.method = "head";
        return this;
    }

    public AyoRequest actionPatch(){
        this.method = "patch";
        return this;
    }

    public AyoRequest stringEntity(String entity){
        this.stringEntity = entity;
        return this;
    }

    public AyoRequest url(String url){
        this.url = url;
        return this;
    }

    public AyoRequest callback(BaseHttpCallback<String> h){
        this.callback = h;
        return this;
    }

//    public <T> Observable<T> start(TypeToken<T> typeToken){
//        ObservableOnSubscribe<T> os = new ObservableOnSubscribe<T>() {
//            @Override
//            public void subscribe(final ObservableEmitter<T> e) throws Exception {
//                callback = new BaseHttpCallback<T>() {
//                    @Override
//                    public void onFinish(boolean isSuccess, HttpProblem problem, FailInfo resp, T t) {
//                        if(isSuccess){
//                            e.onNext(t);
//                        }else{
//                            AyoHttpException ae = new AyoHttpException();
//                            ae.failInfo = resp;
//                            ae.problem = problem;
//                            e.onError(ae);
//                        }
//                        e.onComplete();
//                    }
//                };
//                fire();
//            }
//        };
//        Observable<T> observable = Observable.create(os);
//        return observable;
//    }

    public Object response;

}

package org.cp4j.core.http.impl;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okio.BufferedSource;
import org.cp4j.core.Lang;
import org.cp4j.core.http.AyoRequest;
import org.cp4j.core.http.AyoResponse;
import org.cp4j.core.http.HttpWorker;
import org.cp4j.core.http.OkHttp3BuilderManager;
import org.cp4j.core.http.callback.BaseHttpCallback;
import org.cp4j.core.http.callback.FailInfo;
import org.cp4j.core.http.callback.ProgressRequestListener;
import org.cp4j.core.http.callback.ProgressResponseListener;
import org.cp4j.core.http.impl.cookie.MemoryCookieJar;
import org.cp4j.core.http.impl.progress.ProgressHelper;
import org.cp4j.core.http.impl.progress.ProgressRequestBody;
import org.cp4j.core.http.utils.HttpHelper;

/**
 * Created by Administrator on 2016/8/16.
 */
public class OkHttp3Worker extends HttpWorker {


    private static final class Holder{
        private static final OkHttp3Worker INSTANCE = new OkHttp3Worker();
    }

    public static OkHttp3Worker getDefault(){
        return Holder.INSTANCE;
    }

//    TypeToken typeToken;
    private OkHttpClient okHttpClient;


//    public static void setDnsEnabled(boolean enable){
//        isDnsEnabled = enable;
//    }

    private OkHttp3Worker(){
        OkHttpClient.Builder okHttpClientBuilder = OkHttp3BuilderManager.getOkHttpClientBuilder();
        okHttpClientBuilder.connectTimeout(30000, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.readTimeout(30000, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.writeTimeout(30000, TimeUnit.MILLISECONDS);
//        okHttpClientBuilder.sslSocketFactory(createSSLSocketFactory());
//        okHttpClientBuilder.hostnameVerifier(new TrustAllHostnameVerifier());

        okHttpClientBuilder.cookieJar(new MemoryCookieJar());
//        okHttpClientBuilder.addInterceptor(new GzipRequestInterceptor());
        okHttpClient = okHttpClientBuilder.build();
    }

//    private void init(AyoRequest request){
////        typeToken = request.token;
//    }

    private RequestBody getOkhttpRequestBody(final AyoRequest request, ProgressRequestListener progressRequestListener){
        if(request.method.equalsIgnoreCase("get")){
            return null;
        }else if(request.method.equalsIgnoreCase("head")){
            return null;
        }else{
            //post put delete patch都能传入RequestBody
            boolean hasStringEntity = (request.stringEntity != null && !request.stringEntity.equals(""));
            boolean postFileLikeForm = (request.files != null && request.files.size() > 0);
            boolean postFileLikeStream = (request.file != null && !"".equals(request.file));
            if(!hasStringEntity && !postFileLikeForm && !postFileLikeStream){
                //情况1：postForm
                request.mediaType("application/x-www-form-urlencoded");
                FormBody.Builder builder = new FormBody.Builder();
                if(request.params != null && request.params.size() > 0){
                    for(String key: request.params.keySet()){
                        builder.add(key, request.params.get(key));
                    }
                }
                return  builder.build();
            }else if(hasStringEntity){
                //情况2：postString
                if(request.mediaType == null || "".equals(request.mediaType)){
                    request.mediaType("application/json; charset=utf-8");
                }
                return RequestBody.create(MediaType.parse(request.mediaType), request.stringEntity);
            }else if(postFileLikeStream){
                //情况3：postFile--流形式，不带name，带mime
                if(request.mediaType == null || "".equals(request.mediaType)){
                    request.mediaType("application/json; charset=utf-8");
                }
                return RequestBody.create(MediaType.parse(request.mediaType), new File(request.file));
            }else if(postFileLikeForm){
                //情况4：postFile--multipart形式，带name，带filename
                request.mediaType("multipart/mixed");
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);
                if(request.params != null && request.params.size() > 0){
                    for(String key: request.params.keySet()){
                        builder.addFormDataPart(key, request.params.get(key));
                    }
                }

                if(request.files != null && request.files.size() > 0){
                    for(String key: request.files.keySet()){
                        File f = new File(request.files.get(key));
                        builder.addFormDataPart(key, f.getName(), RequestBody.create(MediaType.parse(HttpHelper.getMediaType(f)), f));
                    }
                }
                RequestBody r = builder.build();
                if(progressRequestListener != null){
                    ProgressRequestBody body = new ProgressRequestBody(r, progressRequestListener);
                    return body;
                }else{
                    return r;
                }
            }else{
                return null;
            }
        }
    }

    private Request parseToOkHttpRequest(AyoRequest request, ProgressRequestListener progressRequestListener){
//        Log.e("22334455", "----1-----new Request.Builder");
        String url = request.url;
        //基于OkHttpUtils辅助类
        Request.Builder builder = new Request.Builder()
                .url(url)
                .tag(request.tag);
        processHeader(builder, request.headers);


        //1 method决定了OkHttpRequestBuilder的哪个子类
        if(request.method.equalsIgnoreCase("get")){
            return builder.get().build();
        }else if(request.method.equalsIgnoreCase("head")){
            return builder.head().build();
        }else if(request.method.equalsIgnoreCase("post")){
            RequestBody body = getOkhttpRequestBody(request, progressRequestListener);
            return builder.post(body).build();
        }else if(request.method.equalsIgnoreCase("put")){
            RequestBody body = getOkhttpRequestBody(request, progressRequestListener);
            return builder.put(body).build();
        }else if(request.method.equalsIgnoreCase("delete")){
            RequestBody body = getOkhttpRequestBody(request, progressRequestListener);
            return builder.delete(body).build();
        }else if(request.method.equalsIgnoreCase("patch")){
            RequestBody body = getOkhttpRequestBody(request, progressRequestListener);
            return builder.patch(body).build();
        }else{
            throw new RuntimeException("使用了不支持的http谓词：" + request.method);
        }
    }

    private void processHeader(Request.Builder builder, Map<String, String> headers){
        if(headers != null && headers.size() > 0){
            for(String key: headers.keySet()){
                builder.addHeader(key, headers.get(key));
            }
        }
    }

    private void fire1(final AyoRequest request, final BaseHttpCallback<String> callback, ProgressRequestListener progressRequestListener, ProgressResponseListener progressResponseListener) {
        Request req = parseToOkHttpRequest(request, progressRequestListener);

        OkHttpClient thisOkHttpClient = this.okHttpClient;
        if(progressResponseListener != null){
            OkHttpClient.Builder thisBuilder = this.okHttpClient.newBuilder();
            ProgressHelper.addProgressResponseListener(thisBuilder,progressResponseListener);
            thisOkHttpClient = thisBuilder.build();
        }

        Call call = thisOkHttpClient.newCall(req);
        tryToAddNewCall(call, req.tag());

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                tryToRemoveCall(call);
                FailInfo failInfo = new FailInfo(-704, e);
                if(callback != null){
                    callback.onFinish(request, false, failInfo, null);
                }

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                tryToRemoveCall(call);
//                final String respStr = response.body().string();

                BufferedSource source = response.body().source();
                try {
                    Charset charset = Util.bomAwareCharset(source, Charset.forName("UTF-8"));
//                    final String respStr = source.readString(charset);
                    final String respStr = source.readUtf8();

                    if(!response.isSuccessful() ){
                        FailInfo failInfo = new FailInfo(response.code(),  respStr);
                        callback.onFinish(request, false,  failInfo, null);
                    }else{
                        AyoResponse r = new AyoResponse();
                        r.data = respStr;
                        if(callback != null){
                            callback.onFinish(request, true, null, r.data);
                        }
                    }
                } finally {
                    Util.closeQuietly(source);
                }


            }
        });
    }

    private Map<String, String> getHeaderMap(Response r){
        Headers headers = r.headers();
        if(headers == null) return new HashMap<>();
        Map<String, String> map = new HashMap<>();
        for(String name: headers.names()){
            map.put(name, r.header(name));
        }
        return map;
    }

    private static Executor executor = new ThreadPoolExecutor(2, 2, 100, TimeUnit.SECONDS,
            new LinkedBlockingDeque<Runnable>(), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("http-" + System.currentTimeMillis());
            return t;
        }
    });

    @Override
    public void fire(final AyoRequest request, final BaseHttpCallback<String> callback, final ProgressRequestListener progressRequestListener, final ProgressResponseListener progressResponseListener) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                final AyoResponse response = fireSync(request, progressRequestListener, progressResponseListener);
                request.response = response;
                if(response != null && response.failInfo != null){
                    if(callback != null){
                        callback.onFinish(request, false, response.failInfo, null);
                    }
                }else{
                    if(callback != null){
                        callback.onFinish(request, true, null, response == null ? "Okhttp异常" : response.data);
                    }
                }
            }
        });


    }



    @Override
    public AyoResponse fireSync(final AyoRequest request, ProgressRequestListener progressRequestListener, ProgressResponseListener progressResponseListener) {
        try {
            return fireSync1(request, progressRequestListener, progressResponseListener);
        }catch (Throwable e){
            e.printStackTrace();
            AyoResponse r = new AyoResponse();
            r.failInfo = new FailInfo(-999, e);
            r.data = null;
            return r;
        }
    }

    public AyoResponse download(String url, File outFile, ProgressResponseListener progressResponseListener){
        Request.Builder builder = new Request.Builder()
                .url(url)
                .tag("download")
                .get();
        Request request = builder.build();

        OkHttpClient thisOkHttpClient = this.okHttpClient;
        if(progressResponseListener != null){
            OkHttpClient.Builder thisBuilder = this.okHttpClient.newBuilder();
            ProgressHelper.addProgressResponseListener(thisBuilder,progressResponseListener);
            thisOkHttpClient = thisBuilder.build();
        }

        Call call = thisOkHttpClient.newCall(request);
        try {
            tryToAddNewCall(call, request.tag());
            Response response = call.execute();
            tryToRemoveCall(call);

            if(!response.isSuccessful()){
                String reason = response.networkResponse().body() == null ? "" : response.networkResponse().body().string();
                if(Lang.isEmpty(reason)){
                    reason = response.body().string();
                }
                FailInfo failInfo = new FailInfo(response.code(),  reason);
                AyoResponse r = new AyoResponse();
                r.failInfo = failInfo;
                r.data = null;
                return r;
            }else{
                AyoResponse r = new AyoResponse();

                InputStream in = response.body().byteStream();
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(outFile);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while((len = in.read(buffer)) > 0){
                        out.write(buffer, 0, len);
                    }
                    r.data = outFile.getAbsolutePath();
                }catch (Exception e){
                    FailInfo failInfo = new FailInfo(-768, "读取文件流出错 -- " + e.getMessage());
                    failInfo.e = e;
                    r.failInfo = failInfo;
                    r.data = null;
                }finally {
                    try {
                        if(in != null) in.close();
                        if(out != null) out.close();
                    }catch (Exception e){
                        //ignore
                    }
                }

                return r;
            }

        } catch (Throwable e) {
            e.printStackTrace();
            tryToRemoveCall(call);
            AyoResponse r = new AyoResponse();
            r.failInfo = new FailInfo(-709, e);
            r.data = null;
            return r;
        }
    }

    private AyoResponse fireSync1(final AyoRequest request, ProgressRequestListener progressRequestListener, ProgressResponseListener progressResponseListener) {
//        init(request);
        Request req = parseToOkHttpRequest(request, progressRequestListener);

        OkHttpClient thisOkHttpClient = this.okHttpClient;
        if(progressResponseListener != null){
            OkHttpClient.Builder thisBuilder = this.okHttpClient.newBuilder();
            ProgressHelper.addProgressResponseListener(thisBuilder,progressResponseListener);
            thisOkHttpClient = thisBuilder.build();
        }


        Call call = thisOkHttpClient.newCall(req);
        try {
            long currentTime = System.currentTimeMillis();
            if(request.id == 0) request.id = currentTime;
            request.startTime = request.endTime = 0;
            request.startTime = currentTime;
            request.requestCount++;
            tryToAddNewCall(call, req.tag());
            Response response = call.execute();
            tryToRemoveCall(call);
            request.endTime = System.currentTimeMillis();

            if(!response.isSuccessful()){
                String reason = response.networkResponse().body() == null ? "" : response.networkResponse().body().string();
                if(Lang.isEmpty(reason)){
                    reason = response.body().string();
                }
                FailInfo failInfo = new FailInfo(response.code(),  reason);
                AyoResponse r = new AyoResponse();
                r.failInfo = failInfo;
                return r;
            }else{
                AyoResponse r = new AyoResponse();
                r.data = response.body().string();
                return r;
            }

        } catch (Throwable e) {
            e.printStackTrace();
            tryToRemoveCall(call);
            AyoResponse r = new AyoResponse();
            r.failInfo = new FailInfo(-706, e);
            r.data = null;
            return r;
        }
    }

    private class CallToCancel{
        public Object tag;
        public Call call;
        public CallToCancel(Object tag, Call call){
            this.tag = tag;
            this.call = call;
        }
    }
    List<CallToCancel> firedCalls = new LinkedList<>();
    private Object lock = new Object();
    private void tryToRemoveCall(Call call){
        synchronized (lock){
            int indexWillBeRemove = -1;
            for(int i = 0; i < firedCalls.size(); i++){
                if(call == firedCalls.get(i).call){
                    indexWillBeRemove = i;
                    break;
                }
            }
            if(indexWillBeRemove >= 0 && indexWillBeRemove < firedCalls.size()){
                firedCalls.remove(indexWillBeRemove);
            }
        }
    }

    private void tryToAddNewCall(Call call, Object tag){
        synchronized (lock){
            firedCalls.add(new CallToCancel(tag, call));
        }
    }
    @Override
    public void cancelAll(Object tag) {
        synchronized (lock){
            for(int i = 0; i < firedCalls.size(); i++){
                if(tag != null && tag.equals(firedCalls.get(i).tag)){
                    Call call = firedCalls.get(i).call;
                    if(call != null) call.cancel();
                }
            }
        }
    }



    /**
     * 默认信任所有的证书
     * TODO 最好加上证书认证，主流App都有自己的证书
     *
     * @return
     */
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory sSLSocketFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllManager()},
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return sSLSocketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}

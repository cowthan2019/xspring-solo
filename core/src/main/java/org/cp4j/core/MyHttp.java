package org.cp4j.core;

import org.cp4j.core.http.AyoRequest;
import org.cp4j.core.http.HttpEmitter;
import org.cp4j.core.http.impl.OkHttp3Worker;

public class MyHttp {

    private static HttpEmitter emitter;

    public static HttpEmitter getEmitter() {
        if (emitter == null) {
            emitter = HttpEmitter.newEmitter().connectionTimeout(30000)
                    .writeTimeout(30000)
                    .readTimeout(30000)
                    .worker(OkHttp3Worker.getDefault());
        }
        return emitter;
    }

    public static void cancel(Object tag) {
        if (emitter != null) {
            emitter.cancelAll(tag.hashCode() + "");
        }
    }

    public static AyoRequest getRequest() {


        AyoRequest r = AyoRequest.request()
                .header("version", "1")
                .header("platform", "s");

        return r;
    }
}

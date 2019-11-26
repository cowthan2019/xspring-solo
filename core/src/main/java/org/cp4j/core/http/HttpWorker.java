package org.cp4j.core.http;


import org.cp4j.core.http.callback.BaseHttpCallback;
import org.cp4j.core.http.callback.ProgressRequestListener;
import org.cp4j.core.http.callback.ProgressResponseListener;

import java.io.File;

/**
 * Created by Administrator on 2016/8/16.
 */
public abstract class HttpWorker {
    public abstract void fire(AyoRequest req, BaseHttpCallback<String> callback, ProgressRequestListener progressRequestListener, ProgressResponseListener progressResponseListener);
    public abstract AyoResponse fireSync(AyoRequest req, ProgressRequestListener progressRequestListener, ProgressResponseListener progressResponseListener);
    public abstract void cancelAll(Object tag);
    public abstract AyoResponse download(String url, File outFile, ProgressResponseListener progressResponseListener);
}

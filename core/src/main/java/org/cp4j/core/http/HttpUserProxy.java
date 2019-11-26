package org.cp4j.core.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by qiaoliang on 2017/12/17.
 */

public interface HttpUserProxy {
    void handleOkHttpBuilder(OkHttpClient.Builder builder);
    void handleCommonHeaderAndParameters(Request.Builder builder);
}

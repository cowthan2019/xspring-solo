package org.cp4j.core.http.impl.cookie;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by cowthan on 2018/10/8.
 */

public class MemoryCookieJar implements CookieJar{

    private ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.put(url.host(), cookies);

//        System.out.println("cookies url: " + url.toString() + "---" + url.host());
        for (Cookie cookie : cookies)
        {
//            System.out.println("cookies: " + cookie.toString());
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url.host());
        if(cookies == null) {
            cookies = new ArrayList<>();
//            System.out.println("cookies: 请求--没存cookie---" + url.host());
        }
        for (Cookie cookie : cookies)
        {
//            System.out.println("cookies: 请求--" + cookie.toString());
        }
        return cookies;

        /*
        ArrayList<Cookie> cookies = new ArrayList<>();
        Cookie cookie = new Cookie.Builder()
                .hostOnlyDomain(url.host())
                .name("SESSION").value("zyao89")
                .build();
        cookies.add(cookie);
         */
    }
}

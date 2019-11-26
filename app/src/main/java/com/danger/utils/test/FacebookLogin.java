package com.danger.utils.test;

import org.cp4j.core.JsonUtils;
import org.cp4j.core.MyHttp;
import org.cp4j.core.http.AyoRequest;
import org.cp4j.core.http.AyoResponse;

/**
 * https://www.cnblogs.com/mengxiao/p/7612301.html
 */
public class FacebookLogin {

    public static final String FB_AUTH_LOGIN_URL = "https://graph.facebook.com/oauth/access_token";
    public static final String FB_USERINFO_URL = "https://graph.facebook.com/me";
    //appid和appSecret 是facebook上申请
    //AppId
    public static final String FB_APP_ID = "15751398427*****";
    //AppSecret
    public static final String FB_APP_KEY = "ac6fb2cda5d855fc20920289a4d*****";
    //获取用户的那些信息
    public static final String FB_USER_FIELDS = "id,cover,email,gender,name,languages,timezone,third_party_id,updated_time";


    /**
     * 怎么弄反正就只是返回：
     *
     * {
     *  id: "112350213520471",
     *  name: "Damon Qiao"
     * }
     * @param accessToken
     */
    public static void checkLoginWithToken(String accessToken) {
        try {
            String userUrl = String.format("%s?access_token=%s&fields=%s",
                    FB_USERINFO_URL, accessToken, FB_USER_FIELDS);

            System.out.println(userUrl);
            AyoRequest request = MyHttp.getRequest()
                    .actionGet()
                    .url(userUrl);
            AyoResponse r = MyHttp.getEmitter().fireSync(request);

            System.out.println(JsonUtils.toJsonPretty(r));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String accessToken = "EAAFqZCC59WXcBAFmeNsfNJBARSSBxmjc3ZC2fQY1vXvbxkDrj2GNk1egn8BtdV3rdyPnJqlvjpvSsZBRZBNmXBZBZB3nFYJZAyUQlORz8ZAEFHb4V7O3FrTPXy8rqMMd98iOxmsgRKDo9ZAbSftpl1MKX5TZAkoynyZAw5Ubfn8UkJP31Eb2qAlgYakuhjFHcw3n7ZAJMBvzymStoEwUku6A55Dska5JALtuKc8ZD";
        checkLoginWithToken(accessToken);

        // 获取code值

//        String urlForCode = "https://www.facebook.com/dialog/oauth?client_id={appId}&redirect_uri={redirectUrl}&code={code}";
//        urlForCode = urlForCode.replace("{appId}", "399106320980343");
//        urlForCode = urlForCode.replace("{redirectUrl}", "https://api-dev.freestyle9527.com:81");
//        urlForCode = urlForCode.replace("{code}", "");
//        System.out.println(urlForCode);
//        // https://www.facebook.com/dialog/oauth?client_id=399106320980343&redirect_uri=http://api-dev.freestyle9527.com:81/api.html&code=
//        AyoRequest request = MyHttp.getRequest()
//                .actionGet()
//                .url(urlForCode);
//        AyoResponse r = MyHttp.getEmitter().fireSync(request);
//
//        System.out.println(JsonUtils.toJsonPretty(r));
//
//        //
//        String code = "AQCASnJLegFegwjv3QpQEBtYKrVtVNdukp_sXI5hRNHvipuBCr2ISZtEztLJ61o3OwmSEzClxy_LB48Wl7oHO5jvGPTpqDvO0eZ1jfOPmebeyKj4A0g8Ti1OdMgWJ1xZPh3cmGW4rIEd_awA-uJ1gYXO5GlaUkWX1eJMI1CjbdSeBBHs-u-Utr2GfR6c5Wog2tTxr8TmVDHjLvjIngYQ8Wef7ZVJHowslhm56vUU1r99U-iULQ800hyDROAkfEK2EV702pATz8dm0hQsvGOkaaTKYQQ37bLkQkzFOPcPoIDMNQKYKbeei7ruSO5_Z9OXNzeRtffy6Hn9Ue5qgG0JlwSj#_=_";
    }

}

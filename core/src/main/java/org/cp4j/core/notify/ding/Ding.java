package org.cp4j.core.notify.ding;

import org.cp4j.core.AssocArray;
import org.cp4j.core.Date2;
import org.cp4j.core.JsonUtils;
import org.cp4j.core.MyHttp;
import org.cp4j.core.http.AyoRequest;
import org.cp4j.core.http.AyoResponse;

public class Ding {


    public static void send(String content, DingConfig config) {
        content = Date2.getCurrentDate() + ": " + content;
        sendToDingDingChatroom(config.getDomain(), content);
    }

    private static void sendToDingDingChatroom(String authUrl, String text) {
        AssocArray array = AssocArray.array();
        array.add("msgtype", "text");
        array.add("text", AssocArray.array().add("content", text));
//        array.add("at", AssocArray.array().add("isAtAll", true));
        AyoRequest r = MyHttp.getRequest().tag("钉钉通知发送")
                .url(authUrl)
                .actionPost()
                .header("content-type", "application/json; charset=utf-8")
                .stringEntity(JsonUtils.toJson(array));
        AyoResponse response = MyHttp.getEmitter().fireSync(r);
        System.out.println("钉钉返回：" + JsonUtils.toJson(response));
    }


}

package org.cp4j.core.notify.mail.sendcloud;

import org.cp4j.core.JsonUtils;
import org.cp4j.core.Lang;
import org.cp4j.core.MyHttp;
import org.cp4j.core.http.AyoRequest;
import org.cp4j.core.http.AyoResponse;

import java.util.List;

public class MyMailSenderBySendCloud {

    public static AyoResponse send(List<String> to, String subject, String text, String summary){
        return send(to, subject, text, summary, null, null);
    }

    public static AyoResponse send(String to, String subject, String text, String summary){
        return send(Lang.newArrayList(to), subject, text, summary, null, null);
    }

    public static AyoResponse send(List<String> to, String subject, String text, String summary, List<String> cc, List<String> bcc) {

        String url = "http://api.sendcloud.net/apiv2/mail/send";

        String tos = Lang.fromList(to, ";", true);
        String ccs = Lang.fromList(cc, ";", true);
        String bccs = Lang.fromList(bcc, ";", true);

        // appkey
        AyoRequest r = MyHttp.getRequest().tag("发邮件")
                .url(url)
                .actionPost()
                // sendcloud测试信息
//                .param("apiUser", "cowthan_test_XGE56u")
//                .param("domain", "TLJVYdHzi0bOMM6XPzDfcXf4SNejQbgg.sendcloud.org")
//                .param("apiKey", "LQl1Q3SIcu7wZRyp")

                // sendcloud正式信息
                .param("apiUser", "cowthan201902")
                .param("domain", "freestyle9527.com")
                .param("apiKey", "p0u1yds23daKzqO")

                .param("from", "qiaoliang@wbd.com")
                .param("to", tos)
                .param("subject", subject)
                .param("html", text)
                .param("contentSummary", summary)
                .param("fromName", "邮件中心")
                .param("cc", ccs)
                .param("bcc", bccs);
        AyoResponse response = MyHttp.getEmitter().fireSync(r);
        return response;
    }

    public static void test(String[] args) {
        AyoResponse response = send(Lang.newArrayList("279800561@qq.com"), "测试", "<h1>ddddd</h1>", "摘要");
        if(response.failInfo == null){
            SendCloudResponse r = JsonUtils.getBean(response.data, SendCloudResponse.class);
            if(r.isResult() && r.getStatusCode() == 200){
                System.out.println("发送成功");
            }else{
                System.out.println("发送失败：" + r.getMessage());
            }
        }
    }
}

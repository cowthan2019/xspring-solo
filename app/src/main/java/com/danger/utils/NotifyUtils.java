package com.danger.utils;

import com.danger.App;
import org.cp4j.core.Lang;
import org.cp4j.core.MyResponse;
import org.cp4j.core.http.AyoResponse;
import org.cp4j.core.notify.mail.ali.AliMailConfig;
import org.cp4j.core.notify.mail.ali.AliyunAccessConfig;
import org.cp4j.core.notify.mail.ali.MyMailSenderByAliyunDirectMail;
import org.cp4j.core.notify.sms.SmsSenderByAliyun;
import org.cp4j.core.utils.Logs;

import java.util.List;

public class NotifyUtils {

    public static MyResponse sendVerifyCodeMail(String to, String verifyCode){
        AliMailConfig config = new AliMailConfig();
        config.setRegion("cn-hangzhou");
        config.setAccessKey("LTAImrMdXHTi1uQ3");
        config.setAccessKeySecret("nE3V6sQcZEl557XvUgeO6XVe9eh6PK");
        config.setSenderAccount("002@freestyle9527.com");

        MyResponse response = MyMailSenderByAliyunDirectMail.send(Lang.newArrayList(to),
                "验证码",
                "您的验证码为<b>{code}</b>，请您在十分钟内使用。".replace("{code}", verifyCode),
                config);
        return response;
    }

    public static AyoResponse sendVerifyCodeSms(String to, String verifyCode){
        AliyunAccessConfig config = new AliyunAccessConfig();
        config.setRegion("cn-hangzhou");
        config.setAccessKey("LTAImrMdXHTi1uQ3");
        config.setAccessKeySecret("nE3V6sQcZEl557XvUgeO6XVe9eh6PK");

        AyoResponse response = SmsSenderByAliyun.sendVerifyCode(to, verifyCode, config);
        return response;
    }

    public static boolean sendEmail(List<String> to, String subject, String content){


        MyResponse response = MyMailSenderByAliyunDirectMail.send(to,subject,content, App.env().getAliMailConfig());
        if(response.getCode() == 0){
            Logs.info("发送成功 -- " + response.getData());
            return true;
        }else{
            Logs.error("发送失败 -- " + response.getMessage());
            return false;
        }
    }
}

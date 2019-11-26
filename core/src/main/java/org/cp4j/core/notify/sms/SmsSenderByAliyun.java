package org.cp4j.core.notify.sms;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.cp4j.core.JsonUtils;
import org.cp4j.core.http.AyoResponse;
import org.cp4j.core.http.callback.FailInfo;
import org.cp4j.core.notify.mail.ali.AliyunAccessConfig;

public class SmsSenderByAliyun {

    public static AyoResponse sendVerifyCode(String phone, String verifyCode, AliyunAccessConfig config) {
        DefaultProfile profile = DefaultProfile.getProfile(config.getRegion(),
                config.getAccessKey(),
                config.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", config.getRegion());
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "则不达");
        request.putQueryParameter("TemplateCode", "SMS_130430023");
        request.putQueryParameter("TemplateParam", "{\"code\":\"{code}\"}".replace("{code}", verifyCode));
        try {
            CommonResponse response = client.getCommonResponse(request);
//            System.out.println(response.getData());
            AliyunSmsResponse r = JsonUtils.getBean(response.getData(), AliyunSmsResponse.class);
            if(r.getCode().equalsIgnoreCase("OK")){
                AyoResponse ar = new AyoResponse();
                ar.data = response.getData();
                return ar;
            }else{
                AyoResponse ar = new AyoResponse();
                ar.failInfo = new FailInfo(400, r.getCode() + ": " + r.getMessage());
                return ar;
            }
        } catch (Exception e) {
            e.printStackTrace();
            AyoResponse ar = new AyoResponse();
            ar.failInfo = new FailInfo(500, e.getMessage());
            return ar;
        }
    }
    /*
    返回：
    {
	"Message":"OK",
	"RequestId":"2184201F-BFB3-446B-B1F2-C746B7BF0657",
	"BizId":"197703245997295588^0",
	"Code":"OK"
}
     */

    public static void test(String[] args) {
        AliyunAccessConfig config = new AliyunAccessConfig();
        config.setRegion("cn-hangzhou");
        config.setAccessKey("LTAImrMdXHTi1uQ3");
        config.setAccessKeySecret("nE3V6sQcZEl557XvUgeO6XVe9eh6PK");
        config.setSenderAccount("002@freestyle9527.com");
        AyoResponse r = sendVerifyCode("15011571302", "323232", config);
        if(r.failInfo == null){
            System.out.println("发送成功: " + r.data);
        }else{
            System.out.println("发送失败：" + r.failInfo.reason);
        }
    }

}

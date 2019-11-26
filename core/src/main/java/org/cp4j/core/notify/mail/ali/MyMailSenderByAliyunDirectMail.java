package org.cp4j.core.notify.mail.ali;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.BatchSendMailRequest;
import com.aliyuncs.dm.model.v20151123.BatchSendMailResponse;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.cp4j.core.JsonUtils;
import org.cp4j.core.Lang;
import org.cp4j.core.MyResponse;
import org.cp4j.core.http.AyoResponse;
import org.cp4j.core.http.callback.FailInfo;

import java.util.List;

/**
 * 阿里云发件设置
 */
public class MyMailSenderByAliyunDirectMail {

    public static MyResponse send(List<String> to, String subject, String text, AliMailConfig config){
        return send(to, subject, text, "", null, null, config);
    }

    public static MyResponse send(String to, String subject, String text, String summary, AliMailConfig config){
        return send(Lang.newArrayList(to), subject, text, summary, null, null, config);
    }

//    public static AyoResponse sendBatch(String subject, String text, String summary){
//        return send(Lang.newArrayList(to), subject, text, summary, null, null);
//    }

    public static MyResponse send(List<String> to, String subject, String text, String summary, List<String> cc, List<String> bcc, AliMailConfig config) {

        // 如果是除杭州region外的其它region（如新加坡、澳洲Region），需要将下面的"cn-hangzhou"替换为"ap-southeast-1"、或"ap-southeast-2"。
        IClientProfile profile = DefaultProfile.getProfile(config.getRegion(), config.getAccessKey(), config.getAccessKeySecret());

        // 如果是除杭州region外的其它region（如新加坡region）， 需要做如下处理
        if(!"cn-hangzhou".equals(config.getRegion())){
            try {
                DefaultProfile.addEndpoint(
                        "dm.{ep}.aliyuncs.com".replace("{ep}", config.getRegion()),
                        config.getRegion(),
                        "Dm",
                        "dm.{ep}.aliyuncs.com".replace("{ep}", config.getRegion())
                );
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }

        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
        try {
            // 如果是除杭州region外的其它region（如新加坡region）,必须指定为2017-06-22
            if(!"cn-hangzhou".equals(config.getRegion())){
                request.setVersion("2017-06-22");
            }

            request.setAccountName(config.getSenderAccount());
            request.setFromAlias("邮件中心");
            request.setAddressType(1);
//            request.setTagName("notify");
            request.setReplyToAddress(true);
            //可以给多个收件人发送邮件，收件人之间用逗号分开，批量发信建议使用BatchSendMailRequest方式
            request.setToAddress(Lang.fromList(to, ",", true));
            //request.setToAddress("邮箱1,邮箱2");
            request.setSubject(subject);
            request.setHtmlBody(text);
            //开启需要备案，0关闭，1开启
            //request.setClickTrace("0");
            //如果调用成功，正常返回httpResponse；如果调用失败则抛出异常，需要在异常中捕获错误异常码；错误异常码请参考对应的API文档;
            SingleSendMailResponse httpResponse = client.getAcsResponse(request);

//            AyoResponse r = new AyoResponse();
//            r.data = JsonUtils.toJson(httpResponse);
//            return r;
            return MyResponse.ok(httpResponse);

        } catch (ServerException e) {
            //捕获错误异常码
//            AyoResponse r = new AyoResponse();
//            r.failInfo = new FailInfo(400, "错误码：" + e.getErrCode() + "," + e.getMessage());
//            e.printStackTrace();
            return MyResponse.error(400, "错误码：" + e.getErrCode() + "," + e.getMessage());
        } catch (ClientException e) {
            //捕获错误异常码
            return MyResponse.error(400, "错误码：" + e.getErrCode() + "," + e.getMessage());
        }catch (Exception e) {
            //捕获错误异常码
//            AyoResponse r = new AyoResponse();
//            r.failInfo = new FailInfo(500, e.getMessage());
//            e.printStackTrace();
//            return r;
            return MyResponse.error(500,  e.getMessage());

        }
    }


    public static AyoResponse sendBatch(String templateName, String receiverName, AliyunAccessConfig config) {

        // 如果是除杭州region外的其它region（如新加坡、澳洲Region），需要将下面的"cn-hangzhou"替换为"ap-southeast-1"、或"ap-southeast-2"。
        IClientProfile profile = DefaultProfile.getProfile(config.getRegion(), config.getAccessKey(), config.getAccessKeySecret());

        // 如果是除杭州region外的其它region（如新加坡region）， 需要做如下处理
        if(!"cn-hangzhou".equals(config.getRegion())){
            try {
                DefaultProfile.addEndpoint(
                        "dm.{ep}.aliyuncs.com".replace("{ep}", config.getRegion()),
                        config.getRegion(),
                        "Dm",
                        "dm.{ep}.aliyuncs.com".replace("{ep}", config.getRegion())
                );
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }

        IAcsClient client = new DefaultAcsClient(profile);
        BatchSendMailRequest request = new BatchSendMailRequest();
        try {
            // 如果是除杭州region外的其它region（如新加坡region）,必须指定为2017-06-22
            if(!"cn-hangzhou".equals(config.getRegion())){
                request.setVersion("2017-06-22");
            }

            request.setAccountName(config.getSenderAccount());
            request.setAddressType(1);
            request.setTemplateName(templateName);
            request.setReceiversName(receiverName);
//            request.set
            BatchSendMailResponse httpResponse = client.getAcsResponse(request);

            AyoResponse r = new AyoResponse();
            r.data = JsonUtils.toJson(httpResponse);
            return r;

        } catch (ServerException e) {
            //捕获错误异常码
            AyoResponse r = new AyoResponse();
            r.failInfo = new FailInfo(400, "错误码：" + e.getErrCode() + "," + e.getMessage());
            e.printStackTrace();
            return r;
        } catch (ClientException e) {
            //捕获错误异常码
            AyoResponse r = new AyoResponse();
            r.failInfo = new FailInfo(400, "错误码：" + e.getErrCode() + "," + e.getMessage());
            e.printStackTrace();
            return r;
        }catch (Exception e) {
            //捕获错误异常码
            AyoResponse r = new AyoResponse();
            r.failInfo = new FailInfo(500, e.getMessage());
            e.printStackTrace();
            return r;
        }
    }

    public static void test(String[] args) {
        AliMailConfig config = new AliMailConfig();
        config.setRegion("cn-hangzhou");
        config.setAccessKey("LTAImrMdXHTi1uQ3");
        config.setAccessKeySecret("nE3V6sQcZEl557XvUgeO6XVe9eh6PK");
        config.setSenderAccount("002@freestyle9527.com");
        MyResponse response = send(Lang.newArrayList("279800561@qq.com"), "测试", "<h1>ddddd</h1>", config);
        if(response.getCode() == 0){
            System.out.println("发送成功 -- " + response.getData());
        }else{
            System.out.println("发送失败 -- " + response.getMessage());
        }
    }

}

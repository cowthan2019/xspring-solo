package com.danger.utils;

import com.alibaba.fastjson.JSONObject;
import org.cp4j.core.MyResponse;
import org.cp4j.core.AssocArray;
import org.cp4j.core.utils.Logs;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;

/*
流程：
前端支付完，付款成功
苹果会返回一个receipt-data，大概长这个样子{"receipt-data" : "MIIaYAYJKoZIhvcNAQcC……"}
将这个值返回给自己服务器，服务器开始验证账单

参数：
大致原理也就是拿着receipt-data请求苹果服务器，苹果会返回给我们详细的账单信息，根据该信息判断账单是否正确
沙箱环境:https://sandbox.itunes.apple.com/verifyReceipt
正式环境:https://buy.itunes.apple.com/verifyReceipt
请求参数:receipt-data
请求方法:POST

返回：
{
  "status": 0,
  "environment": "Sandbox",
  "receipt": {
    "receipt_type": "ProductionSandbox",
    "adam_id": 0,
    "app_item_id": 0,
    "bundle_id": "com.platomix.MicroBusinessManage",
    "application_version": "2.0.0",
    "download_id": 0,
    "version_external_identifier": 0,
    "receipt_creation_date": "2017-06-06 06:35:27 Etc/GMT",
    "receipt_creation_date_ms": "1496730927000",
    "receipt_creation_date_pst": "2017-06-05 23:35:27 America/Los_Angeles",
    "request_date": "2017-06-06 07:13:26 Etc/GMT",
    "request_date_ms": "1496733206549",
    "request_date_pst": "2017-06-06 00:13:26 America/Los_Angeles",
    "original_purchase_date": "2013-08-01 07:00:00 Etc/GMT",
    "original_purchase_date_ms": "1375340400000",
    "original_purchase_date_pst": "2013-08-01 00:00:00 America/Los_Angeles",
    "original_application_version": "1.0",
    "in_app": []
 }
}

status的值
0 支付成功
21000 App Store不能读取你提供的JSON对象
21002 receipt-data域的数据有问题
21003 receipt无法通过验证
21004 提供的shared secret不匹配你账号中的shared secret
21005 receipt服务器当前不可用
21006 receipt合法，但是订阅已过期。服务器接收到这个状态码时，receipt数据仍然会解码并一起发送
21007 receipt是Sandbox receipt，但却发送至生产系统的验证服务
21008 receipt是生产receipt，但却发送至Sandbox环境的验证服务
 */
public class ApplePayUtils {

    public static MyResponse verify(String receipt) {
        String url = "https://buy.itunes.apple.com/verifyReceipt";
        return verify(url, receipt);
    }

    private static MyResponse verify(String url, String receipt) {

        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setAllowUserInteraction(false);
            PrintStream ps = new PrintStream(connection.getOutputStream());
            ps.print("{\"receipt-data\": \"" + receipt + "\"}");
            ps.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String str;
            StringBuffer sb = new StringBuffer();
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
            String resultStr = sb.toString();
            JSONObject result = JSONObject.parseObject(resultStr);

            Logs.warn("苹果支付验证：" + AssocArray.array().add("url", url).add("receipt", receipt).add("result", result));

            if (result != null && result.getInteger("status") == 21007) {   //递归，以防漏单
                return verify("https://sandbox.itunes.apple.com/verifyReceipt", receipt);
            }

            if(result.getInteger("status").equals(0)){
                return MyResponse.ok(result);
            }else{
                return MyResponse.error(result.getInteger("status"), resultStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

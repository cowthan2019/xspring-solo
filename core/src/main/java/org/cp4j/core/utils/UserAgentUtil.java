package org.cp4j.core.utils;

import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import org.cp4j.core.AssocArray;
import org.cp4j.core.Lang;


import java.io.IOException;

/**
 * 解析http user-agent信息，使用uasparser-0.6.1.jar和jregex-1.2_01.jar两个包
 *
 * https://github.com/chetan/UASparser
 * <dependency>
 *     <groupId>cz.mallat.uasparser</groupId>
 *     <artifactId>uasparser</artifactId>
 *     <version>0.6.2</version>
 * </dependency>
 *
 * http://cs-tower-repair.oss-cn-qingdao.aliyuncs.com/cdn/temp/jregex1.2_01-src.jar
 * <dependency>
 *     <groupId>net.sourceforge.jregex</groupId>
 *     <artifactId>jregex</artifactId>
 *     <version>1.2_01</version>
 * </dependency>
 *
 */
/*
安卓拿系统user-agent

try {
    WebView webView = new WebView(context);
    AppSharePreferences.saveUserAgent(context, webView.getSettings().getUserAgentString());
} catch (Exception ignored) {
    ignored.printStackTrace();
}

// 一般会拿到：Mozilla/5.0 (Linux; Android 5.1.1; HUAWEI M100-801W Build/HUAWEIM2-801W) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/39.0.0.0 Safari/537.36
// okhttp不支持header出现中文，所以要注意转一下

public static String getLocalUserAgent(Context context) {
    String webviewAgent = AppSharePreferences.getUserAgent(context);

    // 防止useragent里出现中文，导致okhttp报错，在某些国产手机里容易出现
    // if (!checkNameAndValue("useragent", webviewAgent)) {
    // webviewAgent = "Mozilla/5.0 (Linux; Android 5.1.1; HUAWEI M100-801W
    // Build/HUAWEIM2-801W) AppleWebKit/537.36 (KHTML, like Gecko)
    // Version/4.0 Chrome/39.0.0.0 Safari/537.36";
    // }
    return webviewAgent + " News/" + Const.AppConstant.VERSIONCODE + " Android/"
            + Build.VERSION.RELEASE + " NewsApp/" + Const.AppConstant.VERSIONCODE + " SDK/"
            + Build.VERSION.SDK_INT + " VERSION/"
            + getVersionName() + "dongqiudiClientApp";
}

{appEnName}/{version_code} Android/{Build.VERSION.RELEASE} Device/{deviceId} SDK/{Build.VERSION.SDK_INT} VERSION/{version_name} {appEnName}
{appEnName}/{version_code} iOS/{Build.VERSION.RELEASE} Device/{deviceId} SDK/{Build.VERSION.SDK_INT} VERSION/{version_name} {appEnName}

 */
public class UserAgentUtil {

    static UASparser uasParser = null;

    // 初始化uasParser对象
    static {
        try {
            uasParser = new UASparser(OnlineUpdater.getVendoredInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AssocArray parseUA(String ua){
        if(Lang.isEmpty(ua)) return null;
        String appEnName = "Momoda";
        AssocArray r = AssocArray.array();
        if(!ua.contains(appEnName)) {
            return null;
        }else{
            String[] parts = ua.split(ua.substring(ua.indexOf(appEnName)));
            String versionCode = parts[0].replace(appEnName + "/", "");
            String platform = "";
            if(parts[1].startsWith("Android")) platform = "android";
            if(parts[1].startsWith("iOS")) platform = "ios";
            String deviceId = parts[2].replace("Device/", "");

            r.add("versionCode", Lang.toInt(versionCode));
            r.add("platform", platform);
            r.add("deviceId", deviceId);
        }
        return r;
    }

    public static void main(String[] args)
    {
//        String str = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.130 Safari/537.36";
//        String str = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; en-US; rv:1.9.2.9) Gecko/20100824 Firefox/3.6.9";
        String str = "Mozilla/5.0 (Linux; Android 5.1.1; HUAWEI M100-801W Build/HUAWEIM2-801W) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/39.0.0.0 Safari/537.36";
        System.out.println(str);
        try {
            UserAgentInfo userAgentInfo = UserAgentUtil.uasParser.parse(str);
            System.out.println("操作系统名称："+userAgentInfo.getOsFamily());//
            System.out.println("操作系统："+userAgentInfo.getOsName());//
            System.out.println("浏览器名称："+userAgentInfo.getUaFamily());//
            System.out.println("浏览器版本："+userAgentInfo.getBrowserVersionInfo());//
            System.out.println("设备类型："+userAgentInfo.getDeviceType());
            System.out.println("浏览器:"+userAgentInfo.getUaName());
            System.out.println("类型："+userAgentInfo.getType());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

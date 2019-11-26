package org.cp4j.core;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

import java.util.Date;
import java.util.List;

public final class JsonUtils {



    private JsonUtils() {
    }

    public static String getString(JSONObject jo, String key){
        return getString(jo, key, "");
    }

    public static String getString(JSONObject jo, String key, String defaultValue){
        if(jo == null) return defaultValue;
        if(!jo.containsKey(key)) return defaultValue;
        String val = jo.getString(key);
        return Lang.snull(val, defaultValue);
    }

    public static JSONObject getJSONObject(JSONObject jo, String key){
        JSONObject defaultValue = null;
        if(jo == null) return defaultValue;
        if(!jo.containsKey(key)) return defaultValue;
        JSONObject val = jo.getJSONObject(key);
        return val;
    }

    public static <T> T getBean(String jsonString, com.alibaba.fastjson.TypeReference<T> type) {
        T t = JSON.parseObject(jsonString, type);
        return t;
    }

    public static String toJson(Object bean) {
        if (bean == null) {
            return null;
        }
        JSONObject.DEFFAULT_DATE_FORMAT="yyyy-MM-dd HH:mm:ss";//设置日期格式

        return JSON.toJSONString(bean, SerializerFeature.WriteDateUseDateFormat);
    }

    public static String toJsonPretty(Object bean) {

        if (bean == null) {
            return null;
        }
        JSONObject.DEFFAULT_DATE_FORMAT="yyyy-MM-dd HH:mm:ss";//设置日期格式

        return JSON.toJSONString(bean, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.PrettyFormat);
    }

    public static <T> List<T> getBeanList(String jsonArrayString, Class<T> cls) {

        List<T> beanList = JSON.parseArray(jsonArrayString, cls);
        return beanList;
    }

    public static <T> T getBean(String jsonString, Class<T> cls) {
        try {
            T t = JSON.parseObject(jsonString, cls);
            return t;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Data
    private static class Bean{
        private boolean flag = true;

        @JSONField(format="yyyy-MM-dd HH:mm")
        private Date date = new Date();

    }

    public static void main(String[] args){
//        OcrMsgResponse bean = getBean("{\"success\":1,\"msgs\" :[]}", OcrMsgResponse.class);
        System.out.println(JsonUtils.toJsonPretty(new Bean()));

        String json = "{\n" +
                "\t\"date\":\"2019-10-19 22:36:51\",\n" +
                "\t\"flag\":true\n" +
                "}\n";
        Bean bean = JsonUtils.getBean(json, Bean.class);
        System.out.println(JsonUtils.toJsonPretty(bean));

    }

}

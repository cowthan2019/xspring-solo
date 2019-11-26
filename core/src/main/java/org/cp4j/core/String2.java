package org.cp4j.core;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class String2 {

    public static boolean isEmail(String s){
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isMobile(String s){
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][2,3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(s);
        b = m.matches();
        return b;
    }

    public static boolean isHttpUrl(String urls) {
        boolean isurl = false;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式

        Pattern pat = Pattern.compile(regex.trim());//对比
        Matcher mat = pat.matcher(urls.trim());
        isurl = mat.matches();//判断是否匹配
        if (isurl) {
            isurl = true;
        }
        return isurl;
    }

    /**
     * 元转分，确保price保留两位有效数字
     * @return
     */
    public static int changeY2F(double priceYuan) {
        DecimalFormat df = new DecimalFormat("#.00");
        priceYuan = Double.valueOf(df.format(priceYuan));
        int money = (int)(priceYuan * 100);
        return money;
    }

    /**
     * 分转元，转换为bigDecimal在toString
     * @return
     */
    public static String changeF2Y(int priceFen) {
        return BigDecimal.valueOf(Long.valueOf(priceFen)).divide(new BigDecimal(100)).toString();
    }

    public static byte[] toBytes(String s){
        if(s == null) return new byte[0];
        try {
            return s.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(isHttpUrl("http://cs-tower-repair.oss-cn-qingdao.aliyuncs.com/cdn/temp/gift2.jpeg"));
        System.out.println(changeF2Y(10));
    }



}

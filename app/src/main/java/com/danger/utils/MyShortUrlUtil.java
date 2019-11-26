package com.danger.utils;

import java.security.MessageDigest;
import java.util.Random;

/**
 * @author 文希
 * @create 2019-08-16 17:43
 */
public class MyShortUrlUtil {

    public static void main(String[] args) {
        String sLongUrl = "http://www.51bi.com/jiu/_pv_47147/"; // 原始链接
        System.out.println("长连接：" + sLongUrl);
        String[] aResult = shortUrl(sLongUrl);// 将产生4组6位字符串
        // 打印结果
        for (int i = 0; i < aResult.length; i++) {
            System.out.println("[" + i + "]:" + aResult[i]);
        }
        Random random = new Random();
        int j = random.nextInt(4);//产成4以内随机数
        System.out.println("短连接：" + aResult[j]);
    }

    /*
    算法：

1)将长网址md5生成32位签名串,分为4段, 每段1个字节（即8位）;

2)对这四段循环处理, 取4个字节（32位）, 将他看成16进制串与0x3fffffff(30位1)与操作, 即超过30位的忽略处理;

3)这30位分成6段, 每5位的数字作为字母表的索引取得特定字符, 依次进行获得6位字符串;

4)总的md5串可以获得4个6位串; 取里面的任意一个就可作为这个长url的短url地址;
     */
    public static String[] shortUrl(String url) {
        // 可以自定义生成 MD5 加密字符传前的混合 KEY
        String key = "ncap";
        // 要使用生成 URL 的字符
        String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h",
                "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
                "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
                "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z"

        };
        // 对传入网址进行 MD5 加密
        String hex = md5ByHex(key + url);

        String[] resUrl = new String[4];
        for (int i = 0; i < 4; i++) {

            // 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
            String sTempSubString = hex.substring(i * 8, i * 8 + 8);

            // 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用long ，则会越界
            long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
            String outChars = "";
            for (int j = 0; j < 8; j++) {
                // 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
                long index = 0x0000003D & lHexLong;
                // 把取得的字符相加
                outChars += chars[(int) index];
                // 每次循环按位右移 3 位
                lHexLong = lHexLong >> 3;
            }
            // 把字符串存入对应索引的输出数组
            resUrl[i] = outChars;
        }
        return resUrl;
    }

    /**
     * MD5加密(32位大写)
     *
     * @param src
     * @return
     */
    public static String md5ByHex(String src) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = src.getBytes();
            md.reset();
            md.update(b);
            byte[] hash = md.digest();
            String hs = "";
            String stmp = "";
            for (int i = 0; i < hash.length; i++) {
                stmp = Integer.toHexString(hash[i] & 0xFF);
                if (stmp.length() == 1)
                    hs = hs + "0" + stmp;
                else {
                    hs = hs + stmp;
                }
            }
            return hs.toUpperCase();
        } catch (Exception e) {
            return "";
        }
    }
}

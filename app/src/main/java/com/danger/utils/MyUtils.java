package com.danger.utils;

import com.danger.config.GlobalConfog;
import org.cp4j.core.Lang;
import org.cp4j.core.MD5Utils;

import java.util.UUID;

public class MyUtils {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    public static boolean isValidPassword(String password) {
        if (Lang.isEmpty(password) || Lang.count(password) < 6 || Lang.count(password) > 20) {
            return false;
        }
        return true;
    }

    public static boolean isValidId(Long id){
        return id != null && id > 0;
    }

    public static String encodePassword(String rawPassword, String account) {
        return MD5Utils.encode(rawPassword + GlobalConfog.secret + account);
    }


}


package org.cp4j.core;

public class PathUtils {

    public static String getSuffix(String fileName){
        if(fileName == null) return "";
        if(fileName.indexOf(".") == -1) return "";
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return Lang.snull(suffix, "");
    }
    public static String extractFilename(String uri){
        if(uri == null) return "";
        if(uri.indexOf("/") == -1) return "";
        String filename = uri.substring(uri.lastIndexOf("/") + 1);
        return Lang.snull(filename, "");
    }
}

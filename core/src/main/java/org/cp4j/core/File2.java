package org.cp4j.core;

import java.io.File;

public class File2 {
    public static boolean deleteFileOrDir(String path){
        return deleteFileOrDir(new File(path));
    }
    public static boolean deleteFileOrDir(File path) {
        if (path == null || !path.exists()) {
            return true;
        }
        if (path.isFile()) {
            return path.delete();
        }
        File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                deleteFileOrDir(file);
            }
        }
        return path.delete();
    }

    /**
     * 获取文件长度
     * @param file
     */
    public static long getFileSize(File file) {
        if (file.exists() && file.isFile()) {
            String fileName = file.getName();
            return file.length();
        }
        return 0;
    }

    public static boolean isFileExistsed(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }
}

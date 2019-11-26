package org.cp4j.core;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class IoUtils {

    public static byte[] readBytes(InputStream in){
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[2048];
        try {
            int len;
            while ((len = in.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
            //logger.error(" oss read stream error,data is :{} ", content, e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

    }

    public static byte[] readBytes(MultipartFile multipartFile){
        try {
            return multipartFile.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void file_put_content(String file, String content){
        if(content == null) return;
        createFileIfNotExists(file);
        PrintWriter pw = null;
        FileOutputStream fs = null;
        try {
            fs = new FileOutputStream(file);
            pw = new PrintWriter(fs);
            pw.write(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(pw != null){
                pw.close();
            }
            if(fs != null){
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void file_append_content(String file, String content){
        if(content == null) return;
        createFileIfNotExists(file);
        PrintWriter pw = null;
        FileOutputStream fs = null;
        try {
            fs = new FileOutputStream(file, true);
            pw = new PrintWriter(fs);
            pw.append(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(pw != null){
                pw.close();
            }
            if(fs != null){
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String file_get_content(String file){
        File f = new File(file);
        if(!f.exists() || !f.isFile()) return "";
        try {
            FileInputStream fs = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            int len = 0;
            StringBuffer sb =new StringBuffer();
            while((len = fs.read(buffer)) != -1){
                sb.append(new String(buffer, 0, len));
            }
            fs.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String file_get_content(InputStream file){
        try {
            InputStream fs = file;
            byte[] buffer = new byte[1024];
            int len = 0;
            StringBuffer sb =new StringBuffer();
            while((len = fs.read(buffer)) != -1){
                sb.append(new String(buffer, 0, len));
            }
            fs.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String file_get_content(Reader file){
        try {
            char[] buffer = new char[1024];
            int len = 0;
            StringBuffer sb =new StringBuffer();
            while((len = file.read(buffer)) != -1){
                sb.append(new String(buffer, 0, len));
            }
            file.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean createFileIfNotExists(String file){
        File f = new File(file);
        File dir = f.getParentFile();
        if(dir != null){
            if(!dir.exists() || !dir.isDirectory()){
                if(!dir.mkdirs()){
                    return false;
                }
            }
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }else{
            return false;
        }
    }

    public static InputStream fromFile(String filePath){
        return fromFile(new File(filePath));
    }

    public static InputStream fromFile(File file){
        if(!file.exists() || !file.isFile()) return null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fs;
    }

    public static void copy(InputStream in, OutputStream out){
        try {
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = in.read(buffer)) != -1){
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            }catch (Exception e){

            }
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable ignored) {
            }
        }
    }

}

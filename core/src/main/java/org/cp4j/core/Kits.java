package org.cp4j.core;

import org.cp4j.core.utils.Logs;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Kits {



    public static AssocArray parseRequest(HttpServletRequest request) {

        AssocArray r = AssocArray.array();
        r.add("uri", request.getRequestURI());
        r.add("method", request.getMethod());
        r.add("remote_host", request.getRemoteHost());
        r.add("remote_port", request.getRemotePort());
        r.add("remote_user", request.getRemoteUser());
        r.add("remote_addr", request.getRemoteAddr());
        r.add("scheme", request.getScheme());
//        r.add("query_string", request.getQueryString());

        AssocArray headers = AssocArray.array();
        Enumeration<String> requestHeaders = request.getHeaderNames();
        while (requestHeaders.hasMoreElements()) {
            String header = requestHeaders.nextElement();
            headers.add(header.toLowerCase(), request.getHeader(header));
        }
        r.add("headers", headers);

        AssocArray cookies = AssocArray.array();
        Cookie[] requestCookies = request.getCookies();
        if (requestCookies != null) {
            for (Cookie c : requestCookies) {
//                cookies.add(c.getName(), parseCookie(c));
                cookies.add(c.getName(), c.getValue());
            }
        }
        r.add("cookies", cookies);

        AssocArray params = AssocArray.array();
        Enumeration<String> requestParams = request.getParameterNames();
        while (requestParams.hasMoreElements()) {
            String param = requestParams.nextElement();
            try {
                params.add(param, request.getParameter(param));
            } catch (Exception e) {
                params.add(param, request.getParameterValues(param));
            }
        }
        r.add("params", params);


        AssocArray parts = AssocArray.array();
        try {
            Collection<Part> requestParts = request.getParts();
            if (requestParts != null) {
                for (Part part : requestParts) {
                    AssocArray p = parsePart(part);
                    parts.add(part.getName(), p);
                }
            }

            r.add("parts", parts);
        } catch (IOException e) {
//            e.printStackTrace();
            r.add("error", "getParts IOException");
        } catch (Exception e) {
//            e.printStackTrace();
            r.add("error", "getParts Exception");
        }


        try {
            // request.getInputStream()到这里已经关闭了，咋办
            String contentType = headers.getString("content-type", "");
            long contentLength = headers.getLong("content-length", 0);
            if (contentType.startsWith("application/json")) {
                String body = IoUtils.file_get_content(request.getInputStream());
                r.add("body", body);
                r.add("body_length", contentLength);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return r;
    }


    private static AssocArray parsePart(Part part) {
        AssocArray a = AssocArray.array();
        a.add("content_type", part.getContentType());
        a.add("name", part.getName());
        a.add("size", part.getSize());
        a.add("submitted_file_name", part.getSubmittedFileName());
        a.add("clazz", part.getClass().getSimpleName());

        if (part.getContentType() == null) {
            // 当成文本处理
            try {
                a.add("value", IoUtils.file_get_content(part.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 当成文件处理
            a.add("value", "这是个文件");
        }

        AssocArray headers = AssocArray.array();
        Collection<String> partHeaders = part.getHeaderNames();
        if (partHeaders != null) {
            for (String header : partHeaders) {
                headers.add(header, part.getHeader(header));
            }
        }

        a.add("headers", headers);
        return a;
    }

    private static AssocArray parseCookie(Cookie c) {
        AssocArray a = AssocArray.array();
        if (c.getName() != null) a.add("name", c.getName());
        if (c.getValue() != null) a.add("value", c.getValue());
        if (c.getComment() != null) a.add("comment", c.getComment());
        if (c.getDomain() != null) a.add("domain", c.getDomain());
        if (c.getMaxAge() != -1) a.add("max_age", c.getMaxAge());
        if (c.getPath() != null) a.add("path", c.getPath());
        if (c.getSecure() != false) a.add("secure", c.getSecure());
        if (c.getVersion() != 0) a.add("version", c.getVersion());
        if (c.isHttpOnly()) a.add("http_only", c.isHttpOnly());
        return a;
    }

    public static void printRequest(HttpServletRequest request){
        Logs.warn(JsonUtils.toJsonPretty(Kits.parseRequest(request)));
    }


    public static String getMac() {
        String mac = "";
        try {
            Process p = new ProcessBuilder("ifconfig").start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                Pattern pat = Pattern.compile("\\b\\w+:\\w+:\\w+:\\w+:\\w+:\\w+\\b");
                Matcher mat = pat.matcher(line);
                if (mat.find()) {
                    mac = mat.group(0);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mac;
    }
}

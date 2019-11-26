package com.danger.app.component.interceptor;

import org.cp4j.core.AssocArray;
import org.cp4j.core.JsonUtils;
import org.cp4j.core.Kits;
import org.cp4j.core.utils.Logs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogInterceptor implements HandlerInterceptor {

    private final Logger _logger = LoggerFactory.getLogger(this.getClass()) ;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object o) throws Exception {
        String requestUri = req.getRequestURI();
        Logs.info("收到请求-pre：{} -- {}", requestUri, o.getClass().getSimpleName());

        // 只有返回true才会继续向下执行，返回false取消当前请求
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object o, ModelAndView modelAndView) throws Exception {
        String requestUri = req.getRequestURI();
        Logs.info("结束请求-post：{}", requestUri);

        if(req.getRequestURI().equals("/error")){
            // 这时modelAndView就是处理error.html的
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object o, Exception e) throws Exception {
        String requestUri = req.getRequestURI();
        //System.out.println("--------" + this.getClass().getSimpleName() + "::afterCompletion【" + requestUri + "】--------" + (e == null ? "" : e.getMessage()));
        AssocArray request = Kits.parseRequest(req);
        String exception = e == null ? "--" : e.getMessage();
        request.add("error", exception);
        Logs.info("结束请求：{}\n{}", requestUri, JsonUtils.toJsonPretty(request));

    }
}

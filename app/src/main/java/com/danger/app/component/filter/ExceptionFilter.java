package com.danger.app.component.filter;

import com.alibaba.fastjson.JSON;
import org.cp4j.core.MyResponse;
import org.cp4j.core.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.NestedServletException;

import javax.servlet.*;
import java.io.IOException;

/**
 * 这里的异常，会被ExceptionAdvice覆盖，这里就捕捉不到异常了
 */
@Deprecated
//@WebFilter(urlPatterns = "/*", filterName = "exceptionFilter")
//@Order(2)
public class ExceptionFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            System.out.println("--------" + this.getClass().getSimpleName() + "::before--------");
            chain.doFilter(request, response);
        } catch (ServiceException e) {
            logger.error(" service error . ", e);
            sendJsonObject(response, MyResponse.error(e.getCode(), e.getMessage()));
        } catch (NestedServletException e) {
            logger.error("servlet nested exception . ", e);
            Throwable t = e.getCause();
            while (t.getCause() != null) {
                t = t.getCause();
            }
            if (t instanceof ServiceException) {
                ServiceException ex = (ServiceException) t;
                sendJsonObject(response, MyResponse.error(ex.getCode(), ex.getMessage()));
            } else {
                logger.error(" error. ", e);
                sendJsonObject(response, MyResponse.error(500, e.getMessage()));
//                .sendEmail(e);
            }
        } catch (Exception e) {
            logger.error(" error. ", e);
            sendJsonObject(response, MyResponse.error(500, e.getMessage()));
//            Lang.sendEmail(e);
        }
        System.out.println("--------" + this.getClass().getSimpleName() + "::after--------");
    }

    @Override
    public void destroy() {

    }

    public void sendJsonObject(ServletResponse response, MyResponse object) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        ServletOutputStream out = response.getOutputStream();
        String info = JSON.toJSONString(object);
        byte[] bytes = info.getBytes("UTF-8");
        out.write(bytes, 0, bytes.length);
        out.flush();
    }
}


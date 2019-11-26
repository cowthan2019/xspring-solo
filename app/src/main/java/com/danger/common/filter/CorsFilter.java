package com.danger.common.filter;

import com.danger.App;
import com.danger.app.component.filter.ExceptionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 跨域用，必须
 */

@WebFilter(urlPatterns = "/*", filterName = "corsFilter")
@Order(1)
public class CorsFilter  implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("--------" + this.getClass().getSimpleName() + "::before--------");
        App.REQUEST_COUNT.incrementAndGet();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

//        httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
//        if(httpServletRequest.getHeader("Origin").contains("woego.cn")) {
//        }

//        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
//        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");


        httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "x-custom-header, authorization, yl-authorization, Origin, X-Requested-With, Content-Type, Accept, " +
                "WG-App-Version, WG-Device-Id, WG-Network-Type, WG-Vendor, WG-OS-Type, WG-OS-Version, WG-Device-Model, WG-CPU, WG-Sid, WG-App-Id, WG-Token");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");

        chain.doFilter(request, response);

        System.out.println("--------" + this.getClass().getSimpleName() + "::after--------");

    }

    @Override
    public void destroy() {

    }

}
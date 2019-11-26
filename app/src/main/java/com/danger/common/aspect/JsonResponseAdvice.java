package com.danger.common.aspect;

import org.cp4j.core.MyResponse;
import org.cp4j.core.response.JsonResponse;
import org.cp4j.core.response.JsonResponseInterceptor;
import org.cp4j.core.utils.Logs;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class JsonResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {

        // 判断是否需要重写请求体（重写就是用MyResponse包一层）
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        JsonResponse ann = (JsonResponse) sra.getAttribute(JsonResponseInterceptor.RESPONSE_RESULT_ANN, 0);
        return ann == null ? false : true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        Logs.info("拦截请求--MyResponse重写");
        if(o == null) return MyResponse.ok();
        if(o instanceof MyResponse) return o;
        if((o instanceof Boolean) && o.equals(Boolean.TRUE)) return MyResponse.ok();
        if((o instanceof String) && o.equals("")) return MyResponse.ok();
        else return MyResponse.ok(o);
    }
}

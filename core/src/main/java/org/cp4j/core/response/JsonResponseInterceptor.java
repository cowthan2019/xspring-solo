package org.cp4j.core.response;


import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class JsonResponseInterceptor implements HandlerInterceptor {

    public static final String RESPONSE_RESULT_ANN = "RESPONSE_RESULT_ANN";

    // 此代码核心思想，就是获取此请求，是否需要返回值包装，设置一个属性标记
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod){
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            final Class<?> clazz = handlerMethod.getBeanType();
            final Method method = handlerMethod.getMethod();
            if(clazz.isAnnotationPresent(JsonResponse.class)){
                request.setAttribute(RESPONSE_RESULT_ANN, clazz.getAnnotation(JsonResponse.class));
            }else if(method.isAnnotationPresent(JsonResponse.class)){
                request.setAttribute(RESPONSE_RESULT_ANN, method.getAnnotation(JsonResponse.class));
            }
        }

        return true;
    }
}

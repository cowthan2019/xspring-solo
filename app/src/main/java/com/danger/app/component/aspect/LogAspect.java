package com.danger.app.component.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.cp4j.core.JsonUtils;
import org.jboss.logging.NDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Order(1)
@Component
public class LogAspect {
    private Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(public * com.danger.http.controller.*.* (..))")
    public void apiLogAspect(){

    }

    @Before("apiLogAspect()")
    public void doBefore(JoinPoint joinPoint){
        System.out.println("--------" + this.getClass().getSimpleName() + "::doBefore--------");

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        //使用log4j的MDC和NDC特性，识别请求方的IP及调用资料，输出到日志
        MDC.put("uri", request.getRequestURI());
        NDC.push(request.getRemoteAddr());

        //记录请求内容
        logger.info("----------------------------");
        logger.info("【URI】" + request.getRequestURI());
        logger.info("【客户端ip地址】" + request.getRemoteAddr());
        logger.info("【请求method】" + request.getMethod());
        logger.info("【Action】" + joinPoint.getSignature().getDeclaringType() + "." + joinPoint.getSignature().getName());
        logger.info("【参数】" + Arrays.toString(joinPoint.getArgs()));
        NDC.pop();
        NDC.clear();
        MDC.get("uri");
        MDC.remove("uri");
    }

    @AfterReturning(returning = "ret", pointcut = "apiLogAspect()")
    public void doAfterReturning(Object ret) throws Throwable{
        System.out.println("--------" + this.getClass().getSimpleName() + "::doAfterReturning--------");

        logger.info("【响应】" + JsonUtils.toJson(ret));
        logger.info("----------------------------");
    }
}

package com.danger.common.log.op;

import com.danger.app.user.service.AuthService;
import com.danger.app.user.model.AuthModel;
import com.danger.common.model.OpLogForm;
import com.danger.common.model.OpLogModel;
import com.danger.common.service.OpLogService;
import com.danger.utils.IpInfoUtil;
import com.danger.utils.ThreadPoolUtil;
import com.danger.utils.useragent.UserAgentInfo;
import com.danger.utils.useragent.UserAgentMgmr;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.cp4j.core.JsonUtils;
import org.cp4j.core.Lang;
import org.cp4j.core.utils.BeanParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Spring AOP实现日志管理
 */
@Aspect
@Component
@Slf4j
public class OpLogAspect {
    private static final ThreadLocal<Date> beginTimeThreadLocal = new NamedThreadLocal<Date>("ThreadLocal beginTime");
    @Autowired
    private OpLogService opLogService;
    @Autowired
    private AuthService authService;

    @Autowired(required = false)
    private HttpServletRequest request;

    /**
     * Controller层切点,注解方式
     */
    //@Pointcut("execution(* *..controller..*Controller*.*(..))")
    @Pointcut("@annotation(com.danger.common.log.op.OpLog)")
    public void controllerAspect() {
    }

    /**
     * 前置通知 (在方法执行之前返回)用于拦截Controller层记录用户的操作的开始时间
     *
     * @param joinPoint 切点
     * @throws InterruptedException
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) throws InterruptedException {
        //线程绑定变量（该数据只有当前请求的线程可见）
        Date beginTime = new Date();
        beginTimeThreadLocal.set(beginTime);
    }

    /**
     * 后置通知(在方法执行之后并返回数据) 用于拦截Controller层无异常的操作
     *
     * @param joinPoint 切点
     */
    @AfterReturning("controllerAspect()")
    public void after(JoinPoint joinPoint) {
        try {
            OpLogForm2 ann = getControllerMethodInfo(joinPoint);
            addLog(ann, request, authService, opLogService);
        } catch (Exception e) {
            log.error("AOP后置通知异常", e);
        }
    }

    public static void addLog(String module, String action, String title, HttpServletRequest request, AuthService authService, OpLogService opLogService){
        OpLogForm2 map = new OpLogForm2();
        map.setTitle(title);
        map.setAction(action);
        map.setModule(module);
        addLog(map, request, authService, opLogService);
    }

    public static void addLog(OpLogForm2 logForm, HttpServletRequest request, AuthService authService, OpLogService opLogService) {
        Long userId = 0L;
        String username = "";
        String title = logForm.getTitle();

        //TODO  如果是application/json呢
        Map<String, String[]> requestParamArr = request.getParameterMap();
        Map<String, String> requestParams = new HashMap<>();
        if (requestParamArr != null) {
            for (String key : requestParamArr.keySet()) {
                String[] valueArr = requestParamArr.get(key);
                if (valueArr == null) {
                    requestParams.put(key, null);
                } else if (valueArr.length == 1) {
                    requestParams.put(key, valueArr[0]);
                } else {
                    requestParams.put(key, Lang.fromArray(valueArr, ", ", false));
                }
            }
        }


        OpLogForm log = new OpLogForm();
        log.setOpTime(new Date());

        //请求用户
        if (!Objects.equals(logForm.getAction(), "login")) {
            //后台操作请求(已登录)
            UserAgentInfo c = UserAgentMgmr.checkRequestCommon(request, true, false, authService);
            if (!c.isOk()) return;

            AuthModel user = c.getAuth();
            username = user.getUsername();
            userId = user.getId();

            log.setUsername(username);
            log.setUserId(userId);
        } else {
            // 登录动作

        }
        log.setTitle(title);
        log.setContent("");
        log.setAction(logForm.getAction());

        log.setRequestUrl(request.getRequestURI());
        log.setRequestType(request.getMethod());
        log.setRequestParam(JsonUtils.toJson(requestParams));

        //其他属性
        log.setIp(IpInfoUtil.getRemoteIp(request));
        // https://blog.csdn.net/HcJsJqJSSM/article/details/84852967
        // https://blog.csdn.net/appjiekou/article/details/49662073
        log.setLocation("");

        log.setModule(logForm.getModule());
        if(requestParams.containsKey("id")){
            if(requestParams.get("id") == null){
                log.setModuleId(0L);
            }else{
                log.setModuleId(Lang.toLong(requestParams.get("id") + ""));
            }
        }else{
            log.setModuleId(0L);
        }

        //.......
        //请求开始时间
        long beginTime = beginTimeThreadLocal.get().getTime();
        long endTime = System.currentTimeMillis();
        //请求耗时
        Long logElapsedTime = endTime - beginTime;
        log.setCost(logElapsedTime);
        //持久化(存储到数据或者ES，可以考虑用线程池)
        //logService.insert(log);
        ThreadPoolUtil.getPool().execute(new SaveSystemLogThread(log, opLogService));
    }

    /**
     * 保存日志至数据库
     */
    private static class SaveSystemLogThread implements Runnable {
        private OpLogForm log;
        private OpLogService logService;

        public SaveSystemLogThread(OpLogForm esLog, OpLogService logService) {
            this.log = esLog;
            this.logService = logService;
        }

        @Override
        public void run() {
            OpLogModel e = BeanParser.obj_to_obj(OpLogModel.class, log, null);
            logService.insertSelective(e);
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public static OpLogForm2 getControllerMethodInfo(JoinPoint joinPoint) throws Exception {
        OpLogForm2 map = new OpLogForm2();
        map.setTitle("");
        map.setAction("");
        map.setModule("");

        //获取目标类名
        String targetName = joinPoint.getTarget().getClass().getName();
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        //获取相关参数
        Object[] arguments = joinPoint.getArgs();
        //生成类对象
        Class targetClass = Class.forName(targetName);
        //获取该类中的方法
        Method[] methods = targetClass.getMethods();
        String title = "";
        String action = "";
        String module = "";
        for (Method method : methods) {
            if (!method.getName().equals(methodName)) {
                continue;
            }
            Class[] clazzs = method.getParameterTypes();
            if (clazzs.length != arguments.length) {
                //比较方法中参数个数与从切点中获取的参数个数是否相同，原因是方法可以重载哦
                continue;
            }
            title = method.getAnnotation(OpLog.class).title();
            action = method.getAnnotation(OpLog.class).action();
            module = method.getAnnotation(OpLog.class).module();
            map.setTitle(title);
            map.setAction(action);
            map.setModule(module);
        }
        return map;
    }
}
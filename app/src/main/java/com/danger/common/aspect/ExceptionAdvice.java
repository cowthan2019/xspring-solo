package com.danger.common.aspect;

import com.danger.common.model.ErrorLogModel;
import com.danger.common.service.ErrorLogService;
import org.cp4j.core.AssocArray;
import org.cp4j.core.Lang;
import org.cp4j.core.MD5Utils;
import org.cp4j.core.MyResponse;
import org.cp4j.core.response.LogicException;
import org.cp4j.core.utils.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.NestedServletException;

import javax.servlet.ServletException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.UUID;

/**
 *
 */
@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {

    @Autowired
    ErrorLogService logService;

    @ExceptionHandler(Exception.class)
    public MyResponse logicException(Exception e) {

        // 返回响应实体内容
        int code = 500;
        String error = "";

        if(e instanceof NestedServletException){
            code = 500;
            if(e instanceof MissingServletRequestParameterException){
                //参数绑定错误
                code = 400;
                error = "参数缺失：" + e.getMessage();
            }
        }else if(e instanceof ServletException){
            if(e instanceof MethodNotAllowedException){
                code = 405;
                error = "不允许的请求类型";
            }else if(e instanceof NoHandlerFoundException){
                code = 404;
                error = "未找到此接口映射";
            }else if(e instanceof HttpRequestMethodNotSupportedException){
                code = 405;
                error = "不允许的请求类型";
            }

        }else if(e instanceof MethodArgumentTypeMismatchException){
            //参数值的类型错误
            code = 400;
            error = "参数类型错误：" + e.getMessage();
        }else if(e instanceof HttpMessageNotReadableException){
            //参数值的类型错误
            code = 400;
            error = "request body参数值类型错误：" + e.getMessage();
        }else if(e instanceof BindException){
            //参数值的类型错误
            code = 400;
            BindException be = (BindException) e;
            List<FieldError> list = be.getFieldErrors();
            StringBuilder sb = new StringBuilder();
            sb.append("参数校验失败：");
            for (int i = 0; i < Lang.count(list); i++){
                FieldError f = list.get(i);
                sb.append(f.getField());
                sb.append(": ");
                sb.append(f.getDefaultMessage());
                sb.append("; ");
            }
            error = sb.toString();
        }else if(e instanceof MethodArgumentNotValidException){
            // RequestBody的Valid走这里
            code = 400;
            error = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        }else if(e instanceof LogicException){
            LogicException le = (LogicException) e;
            code = le.getCode();
            error = le.getError();
        }

        if(Lang.isEmpty(error)){
            Logs.error("遇到业务非预期异常", e);
            error = "服务器内部错误";
            code = 500;
            handleBadError(e);
        }

        return MyResponse.error(code, error);
    }

    public void handleBadError(Exception e){

        boolean needReport = false;
        ErrorLogModel log = null;
        try {
            log = new ErrorLogModel();

            log.setMainId(UUID.randomUUID().toString().replace("-", ""));
            log.setPlatform(3);
            log.setLogType(4);
            log.setIp("0");


            String subject = "后台报警【{title}】".replace("{title}", e.getClass().getSimpleName());
            log.setTitle(subject);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(baos));
            String exception = baos.toString();
            log.setExtra(exception);
            log.setMd5(MD5Utils.encode(exception));

            String sql = "select * from t_error_log where md5 = #{map.md5}";
            AssocArray map = AssocArray.array()
                    .add("md5", log.getMd5());
            List<ErrorLogModel> exsitsLogs = logService.select(sql, map);
            if(Lang.isNotEmpty(exsitsLogs)){
                ErrorLogModel exsitsLog = exsitsLogs.get(0);
                long duration = System.currentTimeMillis() / 1000 - exsitsLog.getLastShowTime();
                log.setFirstShowTime(exsitsLog.getFirstShowTime());
                log.setLastShowTime(System.currentTimeMillis() / 1000);
                log.setTimes(exsitsLog.getTimes() + 1);
                log.setStatus(1);

                logService.update(exsitsLog.getId(),
                        AssocArray.array()
                                .add("last_show_time", log.getLastShowTime())
                                .add("times", log.getTimes())
                                .add("status", log.getStatus())
                                .add("ip", log.getIp())
                );
                if(duration > 5 * Lang.millis_for_1_minute / 1000){
                    // 间隔5分钟了，再报一次
                   needReport = true;
                }
            }else{
                log.setFirstShowTime(System.currentTimeMillis() / 1000);
                log.setLastShowTime(log.getFirstShowTime());
                log.setTimes(1);
                log.setStatus(1);
                logService.insertSelective(log);
               needReport = true;
            }
        }catch (Exception e2){
            e2.printStackTrace();
        }

        if(needReport){
            reportError(log);
        }

    }

    public void reportError(ErrorLogModel log){
    }
}

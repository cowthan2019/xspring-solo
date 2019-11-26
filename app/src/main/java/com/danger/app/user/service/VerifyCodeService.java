package com.danger.app.user.service;


import com.danger.app.user.dao.VerifyCodeDao;
import com.danger.app.user.model.VerifyCodeModel;
import com.danger.utils.NotifyUtils;
import org.cp4j.core.Lang;
import org.cp4j.core.MyResponse;
import org.cp4j.core.Random2;
import org.cp4j.core.String2;
import org.cp4j.core.http.AyoResponse;
import org.cp4j.core.response.LogicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

@Service
public class VerifyCodeService {

    @Autowired
    VerifyCodeDao dao;

    
    @Transactional
    public void add(String account) throws LogicException {
        VerifyCodeModel verifyCodeModel = dao.getByAccount(account);
        if(verifyCodeModel != null) {
            // 检查操作是否频繁
            long deltaTimeMillis = System.currentTimeMillis() - verifyCodeModel.getStartTime().getTime();
            if(deltaTimeMillis < Lang.millis_for_1_minute){
                // 操作太频繁
                LogicException.raise(400, "操作太频繁");
            }else{
                // 生成新code
                String code = createVerifyCode();
                Date startTime = new Date();
                dao.updateCodeByAccount(account, code, startTime);

                sendVerifyCode(account, code);
            }

        }else{
            // 生成新code
            String code = createVerifyCode();
            verifyCodeModel = new VerifyCodeModel();
            verifyCodeModel.setAccount(account);
            verifyCodeModel.setCode(code);
            verifyCodeModel.setStartTime(new Date());
            dao.insertSelective(verifyCodeModel);
            sendVerifyCode(account, code);
        }
    }

    private static Boolean sendVerifyCode(String account, String code) throws LogicException {
        // 发送验证码，获取结果
        if(String2.isEmail(account)){
            MyResponse r = NotifyUtils.sendVerifyCodeMail(account, code);
            if (r.getCode() == 0) {
                return true;
            } else {
                LogicException.raise(400, r.getMessage());
            }
        }else if(String2.isMobile(account)){
            AyoResponse r = NotifyUtils.sendVerifyCodeSms(account, code);
            if (r.failInfo == null) {
                return true;
            } else {
                LogicException.raise(400, r.failInfo.reason);
            }
        }else{
            LogicException.raise(400, "不支持的账号类型");
        }
        return false;
    }

    public static String createVerifyCode(){
        return Random2.getRandomNumbers(4);
    }


    
    public int checkCode(String account, String code){
        VerifyCodeModel verifyCodeModel = dao.getByAccount(account);
        if(verifyCodeModel != null) {
            // 是否相同
            if(Objects.equals(code, verifyCodeModel.getCode())){

                // 无论如何，都要删除
                dao.deleteByAccount(account);

                // 检查是否超时，超时返回3
                long deltaTimeMillis = System.currentTimeMillis() - verifyCodeModel.getStartTime().getTime();
                if(deltaTimeMillis > 10 * Lang.millis_for_1_minute){
                    // 超过10分钟了
                    return 3;
                }else{
                    return 0;
                }

            }else{
                // 验证码不对
                return 2;
            }

        }else{
            // 没有验证码
            return 1;
        }
    }
}

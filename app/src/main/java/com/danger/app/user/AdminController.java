package com.danger.app.user;

import com.danger.ServiceFacade;
import com.danger.app.user.model.AccessToken;
import com.danger.app.user.model.AuthModel;
import com.danger.app.user.model.LoginMorm;
import com.danger.app.user.model.PwdUpdateMorm;
import com.danger.utils.MyUtils;
import com.danger.utils.useragent.UserAgentInfo;
import com.danger.utils.useragent.UserAgentMgmr;
import org.cp4j.core.AssocArray;
import org.cp4j.core.JsonUtils;
import org.cp4j.core.response.JsonResponse;
import org.cp4j.core.response.LogicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class AdminController {

    @Autowired
    ServiceFacade facade;

    @RequestMapping(value = "admin/auth/login", method = {RequestMethod.GET, RequestMethod.POST})
    @JsonResponse
    public AccessToken login(HttpServletRequest request, @Valid LoginMorm form)  throws LogicException {
        System.out.println(JsonUtils.toJsonPretty(form));
        AccessToken token = facade.getAuthService().login(form.getAccount(), form.getPassword(), null, true);
        request.getSession().setAttribute("token", token);
        return token;
    }

    @PostMapping(value = {"admin/auth/pwd_update"})
    @JsonResponse
    public Boolean updatePassword(HttpServletRequest request, @Valid PwdUpdateMorm form)  throws LogicException {
        UserAgentInfo checkInfo = UserAgentMgmr.checkRequestAdmin(request, true, facade.getAuthService());


        if(!form.getNewPassword().equals(form.getNewPassword2())){
            LogicException.raise(400, "两次输入不一致");
        }

        AuthModel authModel = checkInfo.getAuth();
        String oldPassword = MyUtils.encodePassword(form.getOldPassword(), checkInfo.getToken().getUsername());
        if(!oldPassword.equals(authModel.getPassword())){
            LogicException.raise(400, "旧密码错误");
        }

        String newPassword = MyUtils.encodePassword(form.getNewPassword(), checkInfo.getToken().getUsername());
        AssocArray updateValue = AssocArray.array()
                .add("password", newPassword);
        facade.getAuthService().update(authModel.getId(), updateValue);

        return true;
    }

}

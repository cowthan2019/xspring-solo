package com.danger.app.user;

import com.danger.ServiceFacade;
import com.danger.app.user.service.AuthService;
import com.danger.app.user.service.VerifyCodeService;
import com.danger.app.user.model.*;
import com.danger.common.log.access.AccessLog;
import com.danger.utils.MyUtils;
import com.danger.utils.NotifyUtils;
import com.danger.utils.useragent.UserAgentInfo;
import com.danger.utils.useragent.UserAgentMgmr;
import io.swagger.annotations.*;
import org.cp4j.core.AssocArray;
import org.cp4j.core.JsonUtils;
import org.cp4j.core.Lang;
import org.cp4j.core.response.JsonResponse;
import org.cp4j.core.response.LogicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(tags = "账号中心")
@RestController
public class AuthController {


    @Autowired
    private AuthService authService;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Autowired
    private ServiceFacade facade;

    @ApiOperation(value = "检查账号是否已注册")
    @ApiImplicitParam(name = "account", value = "邮箱，手机", required = true, dataType = "String", paramType = "query")
    @GetMapping(value = {"app/auth/check_registered"})
    @JsonResponse
    public RegisteredVO checkRegistered(HttpServletRequest request, @Validated AccountQueryForm queryForm) throws LogicException {
        UserAgentInfo checkInfo = UserAgentMgmr.checkRequestCommon(request, false, authService);
        AuthModel accountModel = authService.getByUsername(queryForm.getAccount());
        return new RegisteredVO(accountModel == null ? 0 : 1);
    }

    @ApiOperation(value = "获取验证码")
    @ApiImplicitParam(name = "account", value = "邮箱，手机", required = true, dataType = "String", paramType = "query")
    @GetMapping("app/auth/get_verify_code")
    @JsonResponse
    public Boolean getVerifyCode(HttpServletRequest request, @Validated AccountQueryForm queryForm)  throws LogicException {
        UserAgentInfo checkInfo = UserAgentMgmr.checkRequestCommon(request, false, authService);
        verifyCodeService.add(queryForm.getAccount());
        return true;
    }


    @AccessLog(title = "注册", module = "user", action = "insert")
    @ApiOperation(value = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "邮箱，手机", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码，6到20位", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "login", value = "是否直接登录，1是，0否，默认是", required = false, dataType = "Integer", paramType = "form")
    })
    @PostMapping("app/auth/register")
    @JsonResponse
    public AccessToken register(HttpServletRequest request, @Validated RegisterForm form)  throws LogicException {
        UserAgentInfo checkInfo = UserAgentMgmr.checkRequestCommon(request, false, authService);
        AuthModel authModel = authService.register(form.getAccount(), form.getPassword(), true, form.getCode(), "", 0, "",3);
        if(form.getLogin() == 1){
            AccessToken token = authService.login(form.getAccount(), form.getPassword(), checkInfo, false);
            return token;
        }else{
            return null;
        }
    }

    @AccessLog(title = "登录", module = "user", action = "select")
    @ApiOperation(value = "登录")
    @ApiResponses({
            @ApiResponse(code=200, response = AccessToken.class, message = ""),
            @ApiResponse(code=400,message="参数问题"),
            @ApiResponse(code=405,message="请求方法错误"),
            @ApiResponse(code=500,message="服务器内部错误"),
            @ApiResponse(code=4000,message="登录失败"),
    })
    @PostMapping(value = {"app/auth/login"})
    @JsonResponse
    public AccessToken login(HttpServletRequest request, @Validated LoginForm form) throws LogicException {
        UserAgentInfo checkInfo = UserAgentMgmr.checkRequestCommon(request, false, authService);
        AccessToken token = authService.login(form.getAccount(), form.getPassword(), checkInfo, false);
        return token;
    }



    @AccessLog(title = "登录[三方]", module = "user", action = "select")
    @ApiOperation(value = "三方平台登录", notes = "facebook参考：https://www.cnblogs.com/mengxiao/p/7612301.html")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "platform", value = "facebook, wx, qq, wb", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "openId", value = "平台返回的用户唯一id，微信是openId，facebook是userId", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "payload", value = "平台附带信息，json形式，如nickname，avatar，email", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping(value = {"app/auth/login_auth2"})
    @JsonResponse
    public AccessToken login(HttpServletRequest request, @Valid RegisterForm2 form) throws LogicException {
        UserAgentInfo checkInfo = UserAgentMgmr.checkRequestCommon(request, false, authService);
        boolean mailResult = NotifyUtils.sendEmail(Lang.newArrayList("cowthan@163.com"),
                "有人通过第三方平台登录",
                "<pre>" + JsonUtils.toJsonPretty(form) + "</pre>");


        // 检查是否已注册，有则进入登录逻辑，无则进入注册逻辑
        String account = "oauth2_" + form.getPlatform() + "_" + form.getOpenId();
        String password = "111111";
        OAuth2PayloadDTO payload = null;
        if(checkInfo.isAndroid()){
            payload = OAuth2PayloadDTO.fromFacebookAndroid(form.getPayload());
        }else if(checkInfo.isIos()){
            payload = OAuth2PayloadDTO.fromFacebookIos(form.getPayload());
        }

        OAuth2BindingModel oAuth2BindingModel = facade.getOAuth2BindingService()
                .getByPlatformAndOpenId(form.getPlatform(), form.getOpenId());
        if(oAuth2BindingModel == null){


            AuthModel authModel = authService.register(account, password, false, "",
                    payload == null ? "" : payload.getName(),
                    payload == null ? 0 : payload.getGender(),
                    payload == null ? "" : payload.getAvatar(),
                    3);

            oAuth2BindingModel = new OAuth2BindingModel();
            oAuth2BindingModel.setOpenId(form.getOpenId());
            oAuth2BindingModel.setPlatform(form.getPlatform());
            oAuth2BindingModel.setName(payload == null ? "" : payload.getName());
            oAuth2BindingModel.setGender(payload == null ? 0 : payload.getGender());
            oAuth2BindingModel.setUserId(authModel.getId());
            facade.getOAuth2BindingService().insertSelective(oAuth2BindingModel);
        }

        AccessToken token = authService.login(account, password, checkInfo, false);
        token.setUserType(3);

        return token;
    }

    @AccessLog(title = "修改密码", module = "user", action = "update")
    @ApiOperation(value = "修改密码", notes = "需登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword", value = "旧密码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping(value = {"app/auth/pwd_update"})
    @JsonResponse
    public Boolean updatePassword(HttpServletRequest request, @Validated PwdUpdateForm form)  throws LogicException {
        UserAgentInfo checkInfo = UserAgentMgmr.checkRequestCommon(request, true, authService);

        AuthModel accountModel = authService.getByUsername(checkInfo.getToken().getUsername());
        String oldPassword = MyUtils.encodePassword(form.getOldPassword(), checkInfo.getToken().getUsername());
        if(!oldPassword.equals(accountModel.getPassword())){
            LogicException.raise(400, "旧密码错误");
        }

        String newPassword = MyUtils.encodePassword(form.getNewPassword(), checkInfo.getToken().getUsername());
        AssocArray updateValue = AssocArray.array()
                .add("password", newPassword);
        authService.update(accountModel.getId(), updateValue);

        return true;
    }


    @AccessLog(title = "重置密码", module = "user", action = "update")
    @ApiOperation(value = "重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "邮箱，手机", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码，6到20位", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "passwordConfirm", value = "密码，6到20位", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping(value = {"app/auth/pwd_reset"})
    @JsonResponse
    public Boolean resetPassword(HttpServletRequest request, @Validated PwdResetForm form) throws LogicException  {
        UserAgentInfo checkInfo = UserAgentMgmr.checkRequestCommon(request, false, authService);

        if(!form.getPassword().equals(form.getPasswordConfirm())){
            LogicException.raise(400, "两次输入不一致");
        }

        // 验证码check
        int check = verifyCodeService.checkCode(form.getAccount(), form.getCode());
        if (check == 0) {

        } else if (check == 3) {
            LogicException.raise(400, "验证码过期");
        } else {
            LogicException.raise(400, "验证码错误");
        }

        AuthModel accountModel = authService.getByUsername(form.getAccount());
        if(accountModel == null){
            LogicException.raise(400, "账号不存在");
        }
        String newPassword = MyUtils.encodePassword(form.getPassword(), form.getAccount());
        AssocArray updateValue = AssocArray.array()
                .add("password", newPassword);
        authService.update(accountModel.getId(), updateValue);

        return true;
    }

    @ApiIgnore
    @GetMapping(value = {"app/auth/verify_token"})
    public UserAgentInfo verifyToken(HttpServletRequest request)  throws LogicException {
        UserAgentInfo checkInfo = UserAgentMgmr.checkRequestCommon(request, false, authService);
        return checkInfo;
    }


    @AccessLog(title = "退出登录", module = "user", action = "update")
    @ApiOperation(value = "退出登录")
    @GetMapping(value = {"app/auth/logout"})
    @JsonResponse
    public Boolean logout(HttpServletRequest request)  throws LogicException {
        UserAgentInfo checkInfo = UserAgentMgmr.checkRequestCommon(request, false, authService);
        return true;
    }


    public static void main(String[] args) {
        String username = "admin";
        String password = "96535_super_2019";  // a016ccfb45e6203830224d9a38521e08
        password = MyUtils.encodePassword(password, username);
        System.out.println(password);

//        AssocArray userinfo = AssocArray.array();
//        userinfo.add("avatar", "aaa")
//                .add("nickname", "昵称1");
//        System.out.println(JsonUtils.toJson(userinfo));
    }

}

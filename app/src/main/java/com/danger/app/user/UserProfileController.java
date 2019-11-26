package com.danger.app.user;

import com.danger.ServiceFacade;
import com.danger.app.user.service.AuthService;
import com.danger.app.user.service.UserProfileService;
import com.danger.app.user.model.*;
import com.danger.common.log.access.AccessLog;
import com.danger.config.GlobalConstant;
import com.danger.utils.useragent.UserAgentInfo;
import com.danger.utils.useragent.UserAgentMgmr;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.cp4j.core.AssocArray;
import org.cp4j.core.response.JsonResponse;
import org.cp4j.core.response.LogicException;
import org.cp4j.core.utils.BeanParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Api(tags ="个人中心")
@RestController
public class UserProfileController {


    @Autowired
    private AuthService authService;

    @Autowired
    private UserProfileService userInfoService;

    @Autowired
    private ServiceFacade facade;

    @ApiOperation(value = "查看用户自己的信息--主页-我的", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping(value = {"app/profile/mine"})
    @JsonResponse
    public UserProfileSimpleVO getMineUserInfo(HttpServletRequest request, @RequestParam String uid)  throws LogicException {
        UserAgentInfo checkInfo = UserAgentMgmr.checkRequestCommon(request, true, authService);
        Long me = checkInfo.getAuth().getId();

        AuthModel peerAccountModel = authService.getByUid(uid);
        if (peerAccountModel == null) {
            LogicException.raise(404, "此用户不存在");
        }

        if(!me.equals(peerAccountModel.getId())){
            LogicException.raise(400, "只能查看自己的个人信息");
        }

        UserProfileModel UserProfileModel = userInfoService.getByUserId(peerAccountModel.getId());
        UserProfileSimpleVO r = BeanParser.obj_to_obj(UserProfileSimpleVO.class, UserProfileModel, null);
        r.setUid(peerAccountModel.getUid());
        r.setSid(peerAccountModel.getSid());

        return r;
    }


    @AccessLog(title = "修改用户信息", module = "user", action = "update")
    @ApiOperation(value = "修改用户信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", value = "昵称，最长64", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "realname", value = "姓名，最长24", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam( name = "gender", value = "性别，0未知，1男，2女", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "headIcon", value = "头像url", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "signature", value = "签名，最长256", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "birth", value = "生日", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "age", value = "年龄", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "mobile", value = "用户mobile，最大长度32字符，非中国大陆手机号码需要填写国家代码(如美国：+1-xxxxxxxxxx)或地区代码(如香港：+852-xxxxxxxx)，可设置为空字符串", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "address", value = "地址", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "extra", value = "附加字段，json形式", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping(value = {"app/profile/update"})
    @JsonResponse
    public Boolean updateUserinfo(HttpServletRequest request, @Valid UserProfileForm form)  throws LogicException {

        UserAgentInfo checkInfo = UserAgentMgmr.checkRequestCommon(request, true, authService);

        Long userId = checkInfo.getAuth().getId();
        UserProfileModel UserProfileModel = userInfoService.getByUserId(userId);
        if(UserProfileModel == null) {
            LogicException.raise(404, "此用户不存在");
        }

        AssocArray data = BeanParser.toMap(form, true);
        if(data == null) data = AssocArray.array();
        if(data.size() == 0){
            return true;
        }
        userInfoService.update(UserProfileModel.getId(), data);

        return true;
    }

    @ApiOperation(value = "查看用户详细资料--看自己的或别人的", notes = "字段详细信息在：http://api-dev.freestyle9527.com:8080/view/doc#/app/%E6%8E%A5%E5%8F%A3%E8%A7%84%E8%8C%83, album(itemCount是相册中照片总数，本接口每个相册最多返回6张照片), gift(giftCount是收到礼物总数，本接口最多返回12个礼物)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping(value = {"app/profile/lookup"})
    @JsonResponse
    public UserProfileDetailVO getUserInfoByUid(HttpServletRequest request, @RequestParam String uid) throws LogicException {
        UserAgentInfo checkInfo = UserAgentMgmr.checkRequestCommon(request, true, authService);

        Long me = checkInfo.getAuth() != null ? checkInfo.getAuth().getId() : 0;
        AuthModel peerAuthModel = authService.getByUid(uid);
        if (peerAuthModel == null) {
            LogicException.raise(404, "此用户不存在");
        }
        Long peerUserId = peerAuthModel.getId();
        boolean isMyself = me.equals(peerUserId);
        boolean isAnchor = peerAuthModel.getRoleId() == GlobalConstant.ROLE_ANCHOR;

        UserProfileModel UserProfileModel = userInfoService.getByUserId(peerUserId);
        UserProfileDetailVO userInfoDetailVO = BeanParser.obj_to_obj(UserProfileDetailVO.class, UserProfileModel, null);

        userInfoDetailVO.setSid(peerAuthModel.getSid());
        userInfoDetailVO.setUid(peerAuthModel.getUid());
        userInfoDetailVO.setRole(peerAuthModel.getRoleId());

        return userInfoDetailVO;
    }





}
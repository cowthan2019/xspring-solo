package com.danger.app.version;

import com.danger.ServiceFacade;
import com.danger.app.version.model.AppVersionModel;
import com.danger.utils.useragent.UserAgentInfo;
import com.danger.utils.useragent.UserAgentMgmr;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.cp4j.core.response.JsonResponse;
import org.cp4j.core.response.LogicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@Api(tags ="版本升级")
@RestController
public class AppVersionController {

    @Autowired
    private ServiceFacade facade;

    @ApiOperation(value="获取最新版本号", notes="")
    @ApiImplicitParams({
    })
    @GetMapping(value = {"app/version/latest"})
    @JsonResponse
    public AppVersionModel getLatestVersion(HttpServletRequest request)  throws LogicException {
        UserAgentInfo checkInfo = UserAgentMgmr.checkRequestCommon(request, false, facade.getAuthService());

        String platform = "";
        if("ios".equals(checkInfo.getHeader().getPlatform())){
            platform = "ios";
        }else{
            platform = "android";
        }
        AppVersionModel appVersionModel = facade.getAppVersionService().getLatestVersion(platform);
        return appVersionModel;
    }

}

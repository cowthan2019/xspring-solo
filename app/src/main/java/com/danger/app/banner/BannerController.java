package com.danger.app.banner;

import com.danger.ServiceFacade;
import com.danger.app.banner.model.BannerModel;
import com.danger.app.banner.model.BannerVO;
import com.danger.app.device.DeviceModel;
import com.danger.utils.useragent.UserAgentInfo;
import com.danger.utils.useragent.UserAgentMgmr;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.cp4j.core.Lang;
import org.cp4j.core.response.JsonResponse;
import org.cp4j.core.response.LogicException;
import org.cp4j.core.utils.BeanParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "主页")
@RestController
public class BannerController {

    @Autowired
    private ServiceFacade facade;


    @ApiOperation(value = "轮播图列表", notes = "获取主页banner")
    @ApiImplicitParams({
    })
    @GetMapping(value = {"app/banner/list"})
    @JsonResponse
    public List<BannerVO> getHomePage(HttpServletRequest request, @RequestParam("location") String location) throws LogicException {
        UserAgentInfo checkInfo = UserAgentMgmr.checkRequestCommon(request, false, facade.getAuthService());
        Long me = checkInfo.getAuth() != null ? checkInfo.getAuth().getId() : 0;

        if(!"home".equals(location)){
            LogicException.raise(400, "location必须为指定值");
        }

        // 轮播图
        List<BannerModel> banners = facade.getBannerService().getByLocation(location);
        List<BannerVO> voList = BeanParser.obj_to_obj(BannerVO.class, banners, null);


        // 记录版本号
        if (checkInfo.getHeader() != null && Lang.isNotEmpty(checkInfo.getHeader().getPlatform())) {
            DeviceModel deviceModel = facade.getDeviceService().selectByPlatformAndDeviceId(checkInfo.getHeader().getPlatform(), checkInfo.getHeader().getDeviceId());
            if (deviceModel == null) {
                deviceModel = new DeviceModel();
            }
            deviceModel.setAppVersion(checkInfo.getHeader().getVersion());
            deviceModel.setDeviceId(checkInfo.getHeader().getDeviceId());
            deviceModel.setPlatform(checkInfo.getHeader().getPlatform());
            deviceModel.setGmtModified(null);
            if (deviceModel.getId() != null) {
                facade.getDeviceService().updateByIdSelective(deviceModel);
            } else {
                facade.getDeviceService().insertSelective(deviceModel);
            }

        }

        return voList;
    }

}

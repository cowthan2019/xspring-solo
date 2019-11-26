package com.danger;

import com.danger.app.banner.service.BannerService;
import com.danger.app.device.DeviceService;
import com.danger.app.user.service.AuthService;
import com.danger.app.user.service.OAuth2BindingService;
import com.danger.app.version.service.AppVersionService;
import com.danger.common.service.RunLogService;
import com.danger.common.service.SchemeService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class ServiceFacade {

    @Autowired
    private AuthService authService;

    @Autowired
    private RunLogService runLogService;

    @Autowired
    private OAuth2BindingService oAuth2BindingService;

    @Autowired
    private BannerService bannerService;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private DeviceService deviceService;


    @Autowired
    private AppVersionService appVersionService;
}

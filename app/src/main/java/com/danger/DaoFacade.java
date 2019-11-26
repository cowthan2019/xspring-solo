package com.danger;

import com.danger.app.banner.dao.BannerDao;
import com.danger.app.device.DeviceDao;
import com.danger.app.user.dao.AuthDao;
import com.danger.app.user.dao.UserProfileDao;
import com.danger.common.dao.RunLogDao;
import com.danger.common.dao.SchemeDao;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class DaoFacade {


    @Autowired
    private AuthDao authDao;

    @Autowired
    private SchemeDao schemeDao;

    @Autowired
    private RunLogDao runLogDao;

    @Autowired
    private UserProfileDao userProfileDao;


    @Autowired
    private DeviceDao deviceDao;


    @Autowired
    private BannerDao bannerDao;

}

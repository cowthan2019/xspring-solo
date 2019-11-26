package com.danger.config;

import com.alibaba.fastjson.annotation.JSONField;
import com.danger.ServiceFacade;
import lombok.Data;
import org.cp4j.core.notify.mail.ali.AliMailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sm")
@Data
public class Env {

    @JSONField(serialize = false, deserialize = false)
    @Autowired
    ServiceFacade facade;

    @Value("${spring.profiles.active}")
    private String profile;

    @Value("${app.file.upload-dir}")
    private String fileUploadDir;

    @Value("${app.file.error}")
    private String errorDir;


    private AliMailConfig aliMailConfig;


    public static void init(){

//        Lang.createFileIfNotExists(App.env().getFileUploadDir() + "aa.txt");
//        Lang.createFileIfNotExists(App.env().getErrorDir() + "aa.txt");

    }

    public void initMailForAli(){
        aliMailConfig = new AliMailConfig();
        aliMailConfig.setRegion("cn-hangzhou");
        aliMailConfig.setAccessKey("LTAsdfsdfTi1uQ3");
        aliMailConfig.setAccessKeySecret("nE3Vdsafsdfsfd6PK");
        aliMailConfig.setSenderAccount("23s5@dasdf.com");
    }
}

package com.danger.common.log.run;

import com.danger.common.model.RunLogForm;
import org.cp4j.core.AssocArray;
import org.cp4j.core.JsonUtils;

import java.net.InetAddress;

public class RunLogBuilder {

    public static RunLogForm createLog(String mainId, String type, AssocArray body){
        RunLogForm log = new RunLogForm();
        AssocArray logBody = AssocArray.array();

        log.setPlatform("server");
        log.setLogType(type);
        log.setMainId(mainId);
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
            String hostName = addr.getHostName(); //获取本机计算机名称
            log.setIp(hostName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.setStatus("");
        log.setTitle("");

        if(body != null){
            logBody.putAll(body);
        }
        log.setBody(JsonUtils.toJson(body));
        return log;
    }

}

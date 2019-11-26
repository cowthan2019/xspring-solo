package org.cp4j.core.notify.mail.ali;

import lombok.Data;

@Data
public class AliyunAccessConfig {

    // 如果是除杭州region外的其它region（如新加坡、澳洲Region），需要将下面的"cn-hangzhou"替换为"ap-southeast-1"、或"ap-southeast-2"。
    private String region = "cn-hangzhou";

    private String accessKey;

    private String accessKeySecret;

    private String senderAccount;
}

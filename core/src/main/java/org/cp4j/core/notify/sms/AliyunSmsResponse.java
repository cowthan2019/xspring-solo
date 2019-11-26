package org.cp4j.core.notify.sms;

import lombok.Data;

@Data
public class AliyunSmsResponse {

    private String Message;
    private String RequestId;
    private String BizId;
    private String Code;

}

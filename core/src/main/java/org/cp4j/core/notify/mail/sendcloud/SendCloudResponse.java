package org.cp4j.core.notify.mail.sendcloud;

import lombok.Data;

@Data
public class SendCloudResponse {

    private boolean result;
    private int statusCode;
    private String message;

}

package com.danger.utils.useragent;

import com.danger.app.user.model.AuthModel;
import lombok.Data;
import org.cp4j.core.MyResponse;

@Data
public class UserAgentInfo {
    private MyResponse response;

    private boolean ok;

    private HeaderInfo header;
    private TokenInfo token;
    private AuthModel auth;

    public static UserAgentInfo fail(MyResponse response) {
        UserAgentInfo c = new UserAgentInfo();
        c.setResponse(response);
        c.setOk(false);
        return c;
    }

    public boolean isAndroid() {
        return header != null && "android".equalsIgnoreCase(header.getPlatform());
    }

    public boolean isIos() {
        return header != null && "ios".equalsIgnoreCase(header.getPlatform());
    }

    public boolean isLogin() {
        return auth != null;
    }

    public int getAppVersion(){
        return header == null ? 0 : header.getVersion();
    }

    public long getMe(){
        return auth == null ? 0 : auth.getId();
    }
}

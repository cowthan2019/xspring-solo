package com.danger.utils.useragent;

import com.danger.app.user.model.AuthModel;
import com.danger.app.user.service.AuthService;
import com.danger.config.GlobalConstant;
import com.danger.utils.JWT;
import org.cp4j.core.AssocArray;
import org.cp4j.core.Lang;
import org.cp4j.core.MyResponse;
import org.cp4j.core.response.LogicException;
import org.cp4j.core.utils.UserAgentUtil;

import javax.servlet.http.HttpServletRequest;

public class UserAgentMgmr {

    public static TokenInfo verifyToken(String headerAuthorization) {
        String auth = headerAuthorization;
        if ((auth != null) && (auth.length() > 7)) {
            String HeadStr = auth.substring(0, 6).toLowerCase();
            if (HeadStr.compareTo("bearer") == 0) {
                auth = auth.substring(7, auth.length());
                AssocArray payload = JWT.unsign(auth);
                if (payload != null) {
                    if (payload.getInt("expired", 0) == 1) {
                        TokenInfo tokenInfo = new TokenInfo();
                        tokenInfo.setExpired(true);
                    } else {
                        TokenInfo tokenInfo = new TokenInfo();
                        tokenInfo.setUsername(payload.getString("username", ""));
                        tokenInfo.setRoleId(payload.getInt("roleId", -1));
                        return tokenInfo;
                    }
                } else {
                }
            }
        }
        return null;
    }

    public static TokenInfo verifyToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        return verifyToken(auth);
    }

    public static HeaderInfo verifyHeader(HttpServletRequest request) {
        HeaderInfo headerInfo = new HeaderInfo();
        String platform = request.getHeader("g_platform");
        String deviceId = request.getHeader("g_deviceId");
        String version = request.getHeader("g_version");
        if (Lang.isNotEmpty(platform)) {
            if (Lang.isNotEmpty(platform) && (platform.equals("android") || platform.equals("ios"))) {
                headerInfo.setPlatform(platform);
            } else {
                return getDefaultHeaderInfo();
            }

            if (Lang.isNotEmpty(deviceId)) {
                headerInfo.setDeviceId(deviceId);
            } else {
                return getDefaultHeaderInfo();
            }

            headerInfo.setVersion(Lang.toInt(version));
        } else {
            AssocArray userAgent = UserAgentUtil.parseUA(request.getHeader("UserAgent"));
            if (userAgent != null) {
                headerInfo.setVersion(userAgent.getInt("versionCode", 99999999));
                headerInfo.setPlatform(userAgent.getString("platform", "android"));
                headerInfo.setDeviceId(userAgent.getString("deviceId", "1"));
            } else {
                return getDefaultHeaderInfo();
            }
        }

        return headerInfo;
    }

    private static HeaderInfo getDefaultHeaderInfo() {
        HeaderInfo headerInfo = new HeaderInfo();
        headerInfo.setVersion(99999999);
        headerInfo.setPlatform("android");
        headerInfo.setDeviceId("1");
        return headerInfo;
    }

    public static UserAgentInfo checkRequestAdmin(HttpServletRequest request, boolean needLogin, AuthService accountService) {
        return checkRequestCommon(request, needLogin, false, accountService);
    }

    public static UserAgentInfo checkRequestCommon(HttpServletRequest request, boolean needLogin, AuthService accountService) throws LogicException {
        UserAgentInfo c = checkRequestCommon(request, needLogin, true, accountService);
        if(!c.isOk()){
            LogicException.raise(c.getResponse().getCode(), c.getResponse().getMessage());
            return null;
        }else{
            return c;
        }
    }

    public static UserAgentInfo checkRequestCommon(HttpServletRequest request, boolean needLogin, boolean needDeviceCheck, AuthService accountService)  {
        // 验证header
        HeaderInfo headerInfo = UserAgentMgmr.verifyHeader(request);
        if (headerInfo == null) {
            return UserAgentInfo.fail(MyResponse.error(4021, "非法的header"));
        }

        // 验证token
        String auth = request.getHeader("Authorization");
        if (needLogin && Lang.isEmpty(auth)) {
            return UserAgentInfo.fail(MyResponse.error(4010, "未登录"));
        }

        TokenInfo tokenInfo = null;
        if (Lang.isNotEmpty(auth)) {
            tokenInfo = UserAgentMgmr.verifyToken(request);
            if (tokenInfo == null) {
                return UserAgentInfo.fail(MyResponse.error(4011, "无效的token"));
            } else if (tokenInfo.isExpired()) {
                return UserAgentInfo.fail(MyResponse.error(4012, "token已过期"));
            }
        }

        // 验证是否封禁，删除
        AuthModel accountModel = null;
        if (tokenInfo != null) {
            accountModel = accountService.getByUsername(tokenInfo.getUsername());

            if (accountModel == null) {
                return UserAgentInfo.fail(MyResponse.error(4016, "用户不存在"));
            }
            if (accountModel.getStatus() == GlobalConstant.USER_STATUS_FORBIDDEN) {
                return UserAgentInfo.fail(MyResponse.error(4013, "用户已被封禁"));
            }
            if (accountModel.getDeleted() == 1) {
                return UserAgentInfo.fail(MyResponse.error(4014, "用户已被删除"));
            }
            if(needDeviceCheck){
                if (!headerInfo.getPlatform().equals(accountModel.getLastLoginPlatform())) {
                    return UserAgentInfo.fail(MyResponse.error(4015, "您已被踢下线"));
                }
                if (!headerInfo.getDeviceId().equals(accountModel.getLastLoginDevice())) {
                    return UserAgentInfo.fail(MyResponse.error(4015, "您已被踢下线"));
                }
            }

        }
        UserAgentInfo checkInfo = new UserAgentInfo();
        checkInfo.setOk(true);
        checkInfo.setHeader(headerInfo);
        checkInfo.setToken(tokenInfo);
        checkInfo.setAuth(accountModel);

        return checkInfo;
    }


}

package com.danger.app.user.service;

import com.danger.DaoFacade;
import com.danger.app.user.dao.AuthDao;
import com.danger.app.user.model.AccessToken;
import com.danger.app.user.model.AuthModel;
import com.danger.app.user.model.UserProfileModel;
import com.danger.config.GlobalConfog;
import com.danger.config.GlobalConstant;
import com.danger.utils.JWT;
import com.danger.utils.MyUtils;
import com.danger.utils.useragent.UserAgentInfo;
import org.cp4j.core.*;
import org.cp4j.core.response.LogicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 */
@Service
public class AuthService {

    @Transactional
    public AccessToken login(String account, String rawPassword, UserAgentInfo checkInfo, boolean isAdmin) throws LogicException {
        AuthModel accountModel = dao.getByUsername(account);
        if (accountModel == null) {
            LogicException.raise(4000, "账号或密码错误");
        } else {
            String password = MyUtils.encodePassword(rawPassword, account);
            if (!password.equals(accountModel.getPassword())) {
                LogicException.raise(4000, "账号或密码错误");
            } else if (accountModel.getStatus() != GlobalConstant.USER_STATUS_OK) {
                LogicException.raise(4000, "账号已被封禁");
            } else if (accountModel.getDeleted() == 1) {
                LogicException.raise(4000, "账号或密码错误");
            }
        }

        if(isAdmin){
            if (accountModel.getRoleId() != GlobalConstant.ROLE_SUPER_ADMIN && accountModel.getRoleId() != GlobalConstant.ROLE_ADMIN) {
                LogicException.raise(4000, "用户不存在");
            }
        }else{
            if (accountModel.getRoleId() != GlobalConstant.ROLE_ANCHOR && accountModel.getRoleId() != GlobalConstant.ROLE_USER) {
                LogicException.raise(4000, "用户不存在");
            }
        }



        AssocArray payload = AssocArray.array()
                .add("username", accountModel.getUsername())
                .add("roleId", accountModel.getRoleId());
        String token = JWT.sign(payload);

        AccessToken accessToken = new AccessToken();
        accessToken.setId(accountModel.getId());
        accessToken.setAccessToken(token);
        accessToken.setTokenType("bearer");
        accessToken.setExpiresIn(JWT.MAX_AGE / 1000);
        accessToken.setUid(accountModel.getUid());
        accessToken.setRole(accountModel.getRoleId());
        accessToken.setUserType(accountModel.getUserType());

        if(checkInfo != null){
            accountModel.setLastLoginPlatform(checkInfo.getHeader().getPlatform());
            accountModel.setLastLoginDevice(checkInfo.getHeader().getDeviceId());
            accountModel.setLastLoginTime(new Date());
            accountModel.setGmtModified(null);
            dao.updateById(accountModel);

//            AssocArray updateValue = AssocArray.array()
//                    .add("last_login_device", checkInfo.getHeader().getDeviceId())
//                    .add("last_login_platform", checkInfo.getHeader().getPlatform())
//                    .add("last_login_time", new Date());
//            accountDao.update(accountModel.getId(), updateValue);
        }

        return accessToken;
    }


    @Transactional
    //@Transient
    public AuthModel register(String account, String password, boolean needCheckCode, String code, String nickname, int gender, String avatar, int role) throws LogicException {
        // 验证码check
        if (needCheckCode) {
            int check = verifyCodeService.checkCode(account, code);
            if (check == 0) {

            } else if (check == 3) {
                LogicException.raise(400, "验证码过期");
            } else {
                LogicException.raise(400, "验证码错误");
            }
        }


        // 是否重复
        AuthModel accountModel = getByUsername(account);
        if (accountModel != null) {
            LogicException.raise(400, "用户已经存在");
        }

        accountModel = new AuthModel();
        accountModel.setUsername(account);
        accountModel.setRoleId(role);
        accountModel.setVipLevel(0);
        accountModel.setStatus(GlobalConstant.USER_STATUS_OK);
        if (String2.isEmail(account)) accountModel.setUserType(GlobalConstant.USER_TYPE_EMAIL);
        else if (String2.isMobile(account)) accountModel.setUserType(GlobalConstant.USER_TYPE_MOBILE);
        else accountModel.setUserType(GlobalConstant.USER_TYPE_THIRD_PARTY);
        accountModel.setDeleted(0);
        accountModel.setUid(com.danger.utils.MyUtils.generateUUID());
        accountModel.setToken(com.danger.utils.MyUtils.generateUUID());


        // 密码加密
        password = MyUtils.encodePassword(password, account);
        accountModel.setPassword(password);

        // 生成各种id，检查是否重复，入库
        int userCount = count();
        int sidLength = 4;
        if (userCount > 1000 / 3) sidLength = 5;
        if (userCount > 10000 / 3) sidLength = 6;
        if (userCount > 100000 / 3) sidLength = 7; // 十万
        if (userCount > 1000000 / 3) sidLength = 8; // 百万
        if (userCount > 10000000 / 3) sidLength = 9; // 百万
        if (userCount > 100000000 / 3) sidLength = 10; // 千万

        String sid = "";
        int sidCreateTimes = 0;
        int fidCreateTimes = 0;
        while (Lang.isEmpty(sid)) {
            sidCreateTimes++;
            sid = Random2.getRandomNumbers(sidLength);
            if (countBySid(sid) > 0) {
                sid = "";
            }
        }

        accountModel.setSid(sid);

        insertSelective(accountModel);
        Long newId = accountModel.getId();

        // 新建userinfo
        UserProfileModel UserProfileModel = new UserProfileModel();
        UserProfileModel.setUserId(accountModel.getId());
        UserProfileModel.setNickname(Lang.snull(nickname, accountModel.getSid()));
        UserProfileModel.setGender(gender);
        UserProfileModel.setHeadIcon(Lang.snull(avatar, GlobalConfog.defaultAvatar));
        userProfileService.insertSelective(UserProfileModel);

        return accountModel;

    }

    public AuthModel getByUsername(String username) {
        return dao.getByUsername(username);
    }

    public AuthModel getByUid(String uid) {
        return dao.getByUid(uid);
    }

    public int countBySid(String sid) {
        return dao.countBySid(sid);
    }

    @Autowired
    private AuthDao dao;

    @Autowired
    private DaoFacade facade;

    @Autowired
    private VerifyCodeService verifyCodeService;


    @Autowired
    private UserProfileService userProfileService;


    public Long insert(AuthModel e) {
        return dao.insert(e);
    }

    
    public Long insertSelective(AuthModel e) {
        return dao.insertSelective(e);
    }

    
    public Long insertMap(Map<String, Object> map){
        return dao.insertMap(map);
    }

    
    public int deleteByIdReal(Long id){
        return dao.deleteByIdReal(id);
    }

    
    public int deleteByIdFake(Long id){
        return dao.deleteByIdFake(id);
    }

    
    public int deleteReal(String where, Map<String, Object> map){
        return dao.deleteReal(where, map);
    }

    
    public int deleteFake(String where, Map<String, Object> map){
        return dao.deleteFake(where, map);
    }

    
    public int updateById(AuthModel e) {
        return dao.updateById(e);
    }

    
    public int update(Long id, Map<String, Object> map) {
        return dao.update(id, map);
    }

    
    public int updateByIdSelective(AuthModel e) {
        return dao.updateByIdSelective(e);
    }

    
    public int updateWhere(Map<String, Object> contentValues, String where, Map<String, Object> map){
        return dao.updateWhere(contentValues, where, map);
    }

    
    public int count() {
        return dao.count();
    }

    
    public AuthModel getById(Long id) {
        return dao.getById(id);
    }

    
    public List<AuthModel> getAll() {
        return dao.getAll();
    }

    
    public List<AuthModel> getList(int page, int pageSize) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNo(page);
        pageQuery.setPageSize(pageSize);
        return dao.getList(pageQuery);
    }

    
    public List<AuthModel> getByIdList(List<Long> ids) {
        return dao.getByIdList(ids);
    }

    
    public List<AuthModel> selectRaw(String sql, Map<String, Object> map) {
        return dao.selectRaw(sql, map);
    }

    
    public int countRaw(String sql, Map<String, Object> map) {
        return dao.countRaw(sql, map);
    }
}
package com.danger.app.user;

import com.danger.ServiceFacade;
import com.danger.app.user.service.AuthService;
import com.danger.app.user.model.AuthModel;
import com.danger.app.user.model.IdMorm;
import com.danger.app.user.model.UserQueryMorm;
import com.danger.common.log.op.OpLog;
import org.cp4j.core.AssocArray;
import org.cp4j.core.Lang;
import org.cp4j.core.MyResponse;
import org.cp4j.core.PageQuery;
import org.cp4j.core.response.JsonResponse;
import org.cp4j.core.response.LogicException;
import org.cp4j.core.utils.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class AdminUserController {

    @Autowired
    private ServiceFacade facade;

    @Autowired
    private AuthService authService;


    @GetMapping("admin/user/list")
    @JsonResponse
    public MyResponse getUserList(HttpServletRequest request, @Valid UserQueryMorm form) throws LogicException {

        AssocArray r = AssocArray.array();
        if(form.getPage() <= 0) form.setPage(1);
        if(form.getCount() <= 0) form.setCount(20);
        if(form.getRole() < 0) form.setRole(0);
        if(Lang.isEmpty(form.getSortKey())) form.setSortKey("id");
        if(Lang.isEmpty(form.getSortOrder())) form.setSortOrder("asc");
        if(Lang.isEmpty(form.getKeyword())) form.setKeyword("");

        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNo(form.getPage());
        pageQuery.setPageSize(form.getCount());

        String sqlSelect = "select u.id raw_id, u.username, u.role_id role,u.user_type, u.uid, u.sid, u.status, uf.* ";
        String sql =
                "from t_auth u " +
                "left join t_user_profile uf on u.id = uf.user_id " +
                "where 1 = 1 " +
                "and u.deleted = 0 "
                ;
        if(Lang.isNotEmpty(form.getKeyword())){
            sql += "and (u.username like #{map.keyword} or u.sid like #{map.keyword} or uf.nickname like #{map.keyword}) ";
        }
        if(form.getRole() == 0){
            sql += "and u.role_id in (3, 4) ";
        }else{
            sql += "and u.role_id = #{map.role}";
        }
        String sqlLimit = " order by u.{sortKey} {sortOrder}  limit #{map.offset}, #{map.count}"
                    .replace("{sortKey}", form.getSortKey())
                    .replace("{sortOrder}", form.getSortOrder())
                ;

        AssocArray whereArgs = AssocArray.array()
                .add("keyword", "%" + form.getKeyword() + "%")
                .add("role", form.getRole())
                .add("offset", pageQuery.getOffset())
                .add("count", pageQuery.getPageSize());

        Logs.info(sqlSelect + sql + sqlLimit);
        List<Map<String, Object>> list = facade.getSchemeService().select(sqlSelect + sql + sqlLimit, whereArgs);
        int totalCount = facade.getSchemeService().count(sql, whereArgs);

        for (int i = 0; i < Lang.count(list); i++) {
            Map<String, Object> row = list.get(i);
            row.put("id", row.get("raw_id"));
            row.remove("raw_id");
        }
        r.add("list", list);
        r.add("total", totalCount);
        r.add("page", form.getPage());
        r.add("count", form.getCount());

        return MyResponse.ok(r);
    }




    @OpLog(module = "user", action = "update", title = "禁用用户")
    @RequestMapping(value = {"admin/user/forbidden"}, method = {RequestMethod.POST})
    @JsonResponse
    public Boolean changeStatusTo1(@Valid IdMorm form) throws LogicException {
        changeStatus(form.getId(), 1);
        return true;
    }

    @OpLog(module = "user", action = "update", title = "激活用户")
    @RequestMapping(value = {"admin/user/activate"}, method = {RequestMethod.POST})
    @JsonResponse
    public Boolean changeStatusTo0(@Valid IdMorm form) throws LogicException {
        changeStatus(form.getId(), 0);
        return true;
    }

    private void changeStatus(Long id, int status) throws LogicException {
        if(id == null || id <= 0){
            LogicException.raise(400, "请传入合法的id");
        }
        AuthModel data = authService.getById(id);
        if(data == null){
            LogicException.raise(400, "找不到该用户");
        }

        AssocArray map = AssocArray.array().add("status", status);
        int effectRows = authService.update(id, map);

        AssocArray r = AssocArray.array();
        r.add("effect", effectRows);
    }


    @OpLog(module = "user", action = "update", title = "设为主播")
    @RequestMapping(value = {"admin/user/change_to_anchor"}, method = {RequestMethod.POST})
    @JsonResponse
    public Boolean changeRole3(@Valid IdMorm form) throws LogicException {
        changeRole(form.getId(), 3);
        return true;
    }


    @OpLog(module = "user", action = "update", title = "设为用户")
    @RequestMapping(value = {"admin/user/change_to_user"}, method = {RequestMethod.POST})
    @JsonResponse
    public Boolean changeRole4(@Valid IdMorm form) throws LogicException {
        changeRole(form.getId(), 4);
        return true;
    }

    private int changeRole(Long id, Integer role) throws LogicException {

        AuthModel data = authService.getById(id);
        if(data == null){
            LogicException.raise(400, "找不到用户");
        }

        if(role == null || (role != 3 && role != 4)){
            LogicException.raise(400, "请传入合法的role");
        }

        AssocArray map = AssocArray.array().add("role_id", role);
        int effectRows = authService.update(id, map);
        return effectRows;
    }

    @OpLog(module = "user", action = "delete", title = "删除用户")
    @PostMapping("admin/user/delete")
    @JsonResponse
    public Boolean delete(HttpServletRequest request, @Valid IdMorm form) throws LogicException {
        authService.deleteByIdFake(form.getId());
        return true;
    }


}

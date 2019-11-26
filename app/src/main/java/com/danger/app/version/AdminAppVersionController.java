package com.danger.app.version;

import com.danger.ServiceFacade;
import com.danger.app.user.model.IdMorm;
import com.danger.app.version.model.AppVersionModel;
import com.danger.app.version.model.AppVersionMorm;
import com.danger.app.version.model.VersionMO;
import com.danger.app.version.model.VersionQueryMorm;
import com.danger.app.version.service.AppVersionService;
import com.danger.common.log.op.OpLog;
import com.danger.utils.MyUtils;
import io.swagger.annotations.Api;
import org.cp4j.core.*;
import org.cp4j.core.response.JsonResponse;
import org.cp4j.core.response.LogicException;
import org.cp4j.core.utils.BeanParser;
import org.cp4j.core.utils.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


@Api(tags ="")
@RestController
public class AdminAppVersionController {

    @Autowired
    private AppVersionService service;

    @Autowired
    private ServiceFacade facade;

    @OpLog(module = "version", action = "insert", title = "创建新版本")
    @PostMapping(value = {"admin/version/create"})
    @JsonResponse
    public Boolean create(HttpServletRequest request, @Validated AppVersionMorm form) throws LogicException {
        form.setId(null);
        save(form);
        return true;
    }

    @OpLog(module = "version", action = "update", title = "修改版本")
    @PostMapping(value = {"admin/version/edit"})
    @JsonResponse
    public Boolean edit(HttpServletRequest request, @Validated AppVersionMorm form) throws LogicException {
        if (!MyUtils.isValidId(form.getId())) {
            LogicException.raise(400, "请传入合法id");
        }

        AppVersionModel data = service.getById(form.getId());
        if (data == null) {
            LogicException.raise(400, "找不到对应的记录");
        }

        save(form);
        return true;
    }

    private boolean save(AppVersionMorm form) {
        AppVersionModel data = BeanParser.newObject(AppVersionModel.class, form, new HashSet<>());
        if (form.getId() == null || form.getId() <= 0) {
            data.setId(null);
            service.insertSelective(data);
            return true;
        } else {
            int effectRows = service.updateByIdSelective(data);
            return true;
        }
    }

    @OpLog(action = "update", title = "禁用版本", module = "version")
    @RequestMapping(value = {"admin/version/forbidden"}, method = RequestMethod.POST)
    @JsonResponse
    public Boolean changeStatusTo1(HttpServletRequest request,  @Validated IdMorm form) throws LogicException {
        Kits.printRequest(request);
        changeStatus(form.getId(), 1);
        return true;
    }

    @OpLog(action = "update", title = "启用版本", module = "version")
    @PostMapping(value = {"admin/version/activate"})
    @JsonResponse
    public Boolean changeStatusTo0(HttpServletRequest request, @Validated IdMorm form) throws LogicException {
        changeStatus(form.getId(), 0);
        return true;
    }


    private int changeStatus(Long id, int status) throws LogicException {
        if (id == null || id <= 0) {
            LogicException.raise(400, "请传入合法的id");
        }
        AppVersionModel data = service.getById(id);
        if (data == null) {
            LogicException.raise(400, "找不到该条目");
        }

        AssocArray map = AssocArray.array().add("status", status);
        int effectRows = service.update(id, map);

        return effectRows;
    }


    @OpLog(action = "delete", title = "删除版本", module = "version")
    @PostMapping(value = {"admin/version/delete"})
    @JsonResponse
    public Boolean delete(HttpServletRequest request, @Validated IdMorm form) throws LogicException {
        int effectRows = service.deleteByIdFake(form.getId());
        return true;
    }


    @GetMapping(value = {"admin/version/list"})
    @JsonResponse
    public PageVO<VersionMO> getList(HttpServletRequest request, @Validated VersionQueryMorm form) throws LogicException {


        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNo(form.getPage());
        pageQuery.setPageSize(form.getCount());

        String sqlSelect = "select * ";
        String sql =
                "from t_app_version c " +
                        "where 1 = 1 " +
                        "and c.deleted = 0 "
                ;
        if(Lang.isNotEmpty(form.getKeyword())){
            sql += "and (c.version_name like #{map.keyword}) ";
        }
        if(!"all".equalsIgnoreCase(form.getPlatform())){
            sql += "and c.platform = #{map.platform} ";
        }
        String sqlLimit = " order by c.{sortKey} {sortOrder}  limit #{map.offset}, #{map.count}"
                .replace("{sortKey}", "id")
                .replace("{sortOrder}", "desc")
                ;

        AssocArray whereArgs = AssocArray.array()
                .add("keyword", "%" + Lang.snull(form.getKeyword()) + "%")
                .add("platform", form.getPlatform())
                .add("offset", pageQuery.getOffset())
                .add("count", pageQuery.getPageSize());

        Logs.info(sqlSelect + sql + sqlLimit);
        List<AppVersionModel> list = service.selectRaw(sqlSelect + sql + sqlLimit, whereArgs);
        int totalCount = service.countRaw(sql, whereArgs);


        List<VersionMO> voList = BeanParser.obj_to_obj(VersionMO.class, list, null);
        String versionCodes = Lang.fromList(voList, ", ", true, new Lang.StringConverter<VersionMO>() {
            @Override
            public String convert(VersionMO appVersionVO) {
                return appVersionVO.getVersionCode() + "";
            }
        });

        if(Lang.isNotEmpty(voList)){
            String sql2 = "select count(*) count, app_version from t_device group by app_version having app_version in (" + versionCodes + ")";
            List<Map<String, Object>> counts = facade.getSchemeService().select(sql2, null);
            for (VersionMO v: voList){
                for (Map<String, Object> c: counts){
                    if(v.getVersionCode().equals(c.get("app_version"))){
                        v.setDeviceCount(Lang.toInt(c.get("count") + ""));
                    }
                }
            }
        }

        PageVO<VersionMO> vo = new PageVO<>();
        vo.setList(voList);
        vo.setPageSize(form.getCount());
        vo.setTotal(totalCount);
        vo.setCurrentPage(form.getPage());
        return vo;
    }

}

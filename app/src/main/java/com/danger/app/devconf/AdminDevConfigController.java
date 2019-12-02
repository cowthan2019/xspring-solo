package com.danger.app.devconf;

import com.danger.ServiceFacade;
import com.danger.app.user.model.IdMorm;
import com.danger.common.log.op.OpLog;
import com.danger.utils.MyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.cp4j.core.AssocArray;
import org.cp4j.core.Lang;
import org.cp4j.core.PageVO;
import org.cp4j.core.response.JsonResponse;
import org.cp4j.core.response.LogicException;
import org.cp4j.core.utils.BeanParser;
import org.cp4j.core.utils.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

@Api(tags = "全局配置")
@RestController
public class AdminDevConfigController {

    @Autowired
    private DevConfigService service;

    @Autowired
    private ServiceFacade facade;


    @OpLog(module = "devconf", action = "insert", title = "创建配置")
    @PostMapping(value = {"admin/devconf/create"})
    @JsonResponse
    public Boolean create(HttpServletRequest request, @Validated DevConfigMorm form) throws LogicException {
        form.setId(null);
        save(form);
        return true;
    }

    @OpLog(module = "devconf", action = "update", title = "修改配置")
    @PostMapping(value = {"admin/devconf/edit"})
    @JsonResponse
    public Boolean edit(HttpServletRequest request, @Valid DevConfigMorm form) throws LogicException {
        if (!MyUtils.isValidId(form.getId())) {
            LogicException.raise(400, "请传入合法id");
        }

        DevConfigModel data = service.getById(form.getId());
        if (data == null) {
            LogicException.raise(400, "找不到对应的记录");
        }

        save(form);
        return true;
    }

    private long save(DevConfigMorm form) throws LogicException {
        DevConfigModel data = BeanParser.newObject(DevConfigModel.class, form, new HashSet<>());
        
        if (MyUtils.isValidId(form.getId())) {
            int effectRows = service.updateByIdSelective(data);
            return effectRows;
        } else {
            data.setId(null);
            service.insertSelective(data);
            return data.getId();
        }

    }


    @OpLog(module = "devconf", action = "update", title = "修改配置值")
    @RequestMapping(value = {"admin/devconf/update_value"}, method = {RequestMethod.POST})
    @JsonResponse
    public Boolean update(@Valid ConfigMorm2 form) throws LogicException {

        DevConfigModel data = service.getById(form.getId());
        if (data == null) {
            LogicException.raise(400, "找不到配置项");
        }
        String value = form.getVal();

        AssocArray map = AssocArray.array().add("val", value);
        int effectRows = service.update(form.getId(), map);

        return true;
    }

    @ApiOperation(value = "删除", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "", required = true, dataType = "Integer", paramType = "param"),
    })
    @OpLog(module = "devconf", action = "delete", title = "删除配置项")
    @RequestMapping(value = {"admin/devconf/delete"}, method = {RequestMethod.POST})
    @JsonResponse
    public Boolean delete(@Valid IdMorm form) throws LogicException {
        int effectRows = service.deleteByIdFake(form.getId());
        return true;
    }

    // http://localhost:8080/app/conf/list/page?page=1&count=10
    @RequestMapping(value = {"admin/devconf/list"}, method = {RequestMethod.GET})
    @JsonResponse
    public PageVO<DevConfigVO> getList(@RequestParam(value = "filter", required = true) String filter) throws LogicException {

        String sqlSelect = "select * ";
        String sql =
                "from t_dev_config c " +
                        "where 1 = 1 " +
                        "and c.deleted = 0 ";
        if (Lang.isNotEmpty(filter)) {
            sql += "and (c.name like #{map.keyword}) ";
        }

        String sqlLimit = " order by c.id asc";

        AssocArray whereArgs = AssocArray.array()
                .add("keyword", Lang.snull(filter) + "%");

        Logs.info(sqlSelect + sql + sqlLimit);
        List<DevConfigModel> list = service.selectRaw(sqlSelect + sql + sqlLimit, whereArgs);
        int totalCount = service.countRaw(sql, whereArgs);


        List<DevConfigVO> voList = BeanParser.obj_to_obj(DevConfigVO.class, list, null);

        PageVO<DevConfigVO> vo = new PageVO<>();
        vo.setList(voList);
        vo.setPageSize(Lang.count(list));
        vo.setTotal(totalCount);
        vo.setCurrentPage(1);
        return vo;
    }


}

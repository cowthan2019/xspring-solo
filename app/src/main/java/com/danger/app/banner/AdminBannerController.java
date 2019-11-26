package com.danger.app.banner;

import com.danger.ServiceFacade;
import com.danger.app.banner.model.BannerMO;
import com.danger.app.banner.model.BannerModel;
import com.danger.app.banner.model.BannerMorm;
import com.danger.app.banner.model.BannerQueryMorm;
import com.danger.app.banner.service.BannerService;
import com.danger.app.user.model.IdMorm;
import com.danger.common.log.op.OpLog;
import com.danger.utils.MyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.cp4j.core.AssocArray;
import org.cp4j.core.Lang;
import org.cp4j.core.PageQuery;
import org.cp4j.core.PageVO;
import org.cp4j.core.response.JsonResponse;
import org.cp4j.core.response.LogicException;
import org.cp4j.core.utils.BeanParser;
import org.cp4j.core.utils.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;


@Api(tags ="轮播图")
@RestController
public class AdminBannerController {

    @Autowired
    private ServiceFacade facade;

    @Autowired
    private BannerService service;

    @OpLog(module = "banner", action = "insert", title = "创建轮播图")
    @PostMapping(value = {"admin/banner/create"})
    @JsonResponse
    public Boolean create(HttpServletRequest request, @Validated BannerMorm form) throws LogicException {
        form.setId(null);
        save(form);
        return true;
    }

    @OpLog(module = "banner", action = "update", title = "修改轮播图")
    @PostMapping(value = {"admin/banner/edit"})
    @JsonResponse
    public Boolean edit(HttpServletRequest request, @Validated BannerMorm form) throws LogicException {
        if (!MyUtils.isValidId(form.getId())) {
            LogicException.raise(400, "请传入合法id");
        }

        BannerModel data = service.getById(form.getId());
        if (data == null) {
            LogicException.raise(400, "找不到对应的记录");
        }

        save(form);
        return true;
    }

    private long save(BannerMorm form) {
        BannerModel data = BeanParser.newObject(BannerModel.class, form, new HashSet<>());

        if (MyUtils.isValidId(form.getId())) {
            int effectRows = service.updateByIdSelective(data);
            return effectRows;
        } else {
            data.setId(null);
            service.insertSelective(data);
            return data.getId();
        }

    }

    @ApiOperation(value = "禁用", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "", required = true, dataType = "Integer", paramType = "param"),
    })
    @OpLog(module = "banner", action = "update", title = "禁用轮播图")
    @RequestMapping(value = {"admin/banner/forbidden"}, method = {RequestMethod.POST})
    @JsonResponse
    public Boolean changeStatusTo1(@Valid IdMorm form) throws LogicException {
        changeStatus(form.getId(), 1);
        return true;
    }


    @ApiOperation(value = "启用", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "", required = true, dataType = "Integer", paramType = "param"),
    })
    @OpLog(module = "banner", action = "update", title = "启用轮播图")
    @RequestMapping(value = {"admin/banner/activate"}, method = {RequestMethod.POST})
    @JsonResponse
    public Boolean changeStatusTo0(@Valid IdMorm form) throws LogicException {
        changeStatus(form.getId(), 0);
        return true;
    }

    private int changeStatus(Long id, int status) throws LogicException {
        if (!MyUtils.isValidId(id)) {
            LogicException.raise(400, "请传入合法的id");
        }
        BannerModel data = service.getById(id);
        if (data == null) {
            LogicException.raise(400, "找不到该条目");
        }

        AssocArray map = AssocArray.array().add("status", status);
        int effectRows = service.update(id, map);

        return effectRows;
    }

    @ApiOperation(value = "删除", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "", required = true, dataType = "Integer", paramType = "param"),
    })
    @OpLog(module = "banner", action = "delete", title = "删除轮播图")
    @RequestMapping(value = {"admin/banner/delete"}, method = {RequestMethod.POST})
    @JsonResponse
    public Boolean delete(@Valid IdMorm form) throws LogicException {
        int effectRows = service.deleteByIdFake(form.getId());
        return true;
    }

    // http://localhost:8080/app/conf/list/page?page=1&count=10
    @RequestMapping(value = {"admin/banner/list"}, method = {RequestMethod.GET})
    @JsonResponse
    public PageVO<BannerMO> getList(@Valid BannerQueryMorm form) throws LogicException {

        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNo(form.getPage());
        pageQuery.setPageSize(form.getCount());

        String sqlSelect = "select * ";
        String sql =
                "from t_banner c " +
                        "where 1 = 1 " +
                        "and c.deleted = 0 "
                ;
        if(Lang.isNotEmpty(form.getKeyword())){
            sql += "and (c.title like #{map.keyword} or c.summary like #{map.keyword}) ";
        }
        if("home".equalsIgnoreCase(form.getLocation())){
            sql += "and c.location = 'home' ";
        }

        String sqlLimit = " order by c.{sortKey} {sortOrder}  limit #{map.offset}, #{map.count}"
                .replace("{sortKey}", "id")
                .replace("{sortOrder}", "asc")
                ;

        AssocArray whereArgs = AssocArray.array()
                .add("keyword", "%" + Lang.snull(form.getKeyword()) + "%")
                .add("offset", pageQuery.getOffset())
                .add("count", pageQuery.getPageSize());

        Logs.info(sqlSelect + sql + sqlLimit);
        List<BannerModel> list = service.selectRaw(sqlSelect + sql + sqlLimit, whereArgs);
        int totalCount = service.countRaw(sql, whereArgs);

        List<BannerMO> voList = BeanParser.obj_to_obj(BannerMO.class, list, null);

        PageVO<BannerMO> vo = new PageVO<>();
        vo.setList(voList);
        vo.setPageSize(form.getCount());
        vo.setTotal(totalCount);
        vo.setCurrentPage(form.getPage());
        return vo;
    }
}



package com.danger.app.version.service;

import com.danger.DaoFacade;
import com.danger.app.version.dao.AppVersionDao;
import com.danger.app.version.model.AppVersionModel;
import org.cp4j.core.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AppVersionService {

    @Autowired
    private AppVersionDao dao;

    @Autowired
    private DaoFacade facade;

    public AppVersionModel getLatestVersion(String platform) {
        return dao.getLatestVersion(platform);
    }




    public Long insert(AppVersionModel e) {
        return dao.insert(e);
    }


    public Long insertSelective(AppVersionModel e) {
        e.setId(null);
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


    public int updateById(AppVersionModel e) {
        return dao.updateById(e);
    }


    public int update(Long id, Map<String, Object> map) {
        return dao.update(id, map);
    }


    public int updateByIdSelective(AppVersionModel e) {
        return dao.updateByIdSelective(e);
    }


    public int updateWhere(Map<String, Object> contentValues, String where, Map<String, Object> map){
        return dao.updateWhere(contentValues, where, map);
    }


    public int count() {
        return dao.count();
    }


    public AppVersionModel getById(Long id) {
        return dao.getById(id);
    }


    public List<AppVersionModel> getAll() {
        return dao.getAll();
    }


    public List<AppVersionModel> getList(int page, int pageSize) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNo(page);
        pageQuery.setPageSize(pageSize);
        return dao.getList(pageQuery);
    }


    public List<AppVersionModel> getByIdList(List<Long> ids) {
        return dao.getByIdList(ids);
    }


    public List<AppVersionModel> selectRaw(String sql, Map<String, Object> map) {
        return dao.selectRaw(sql, map);
    }


    public int countRaw(String sql, Map<String, Object> map) {
        return dao.countRaw(sql, map);
    }

}

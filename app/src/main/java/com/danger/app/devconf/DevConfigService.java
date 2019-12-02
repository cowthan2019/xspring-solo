package com.danger.app.devconf;


import com.danger.DaoFacade;
import org.cp4j.core.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 */
@Service
public class DevConfigService {

    @Autowired
    private DevConfigDao dao;

    @Autowired
    private DaoFacade facade;

    
    public Long insert(DevConfigModel e) {
        return dao.insert(e);
    }

    
    public Long insertSelective(DevConfigModel e) {
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

    
    public int updateById(DevConfigModel e) {
        return dao.updateById(e);
    }

    
    public int update(Long id, Map<String, Object> map) {
        return dao.update(id, map);
    }

    
    public int updateByIdSelective(DevConfigModel e) {
        return dao.updateByIdSelective(e);
    }

    
    public int updateWhere(Map<String, Object> contentValues, String where, Map<String, Object> map){
        return dao.updateWhere(contentValues, where, map);
    }

    
    public int count() {
        return dao.count();
    }

    
    public DevConfigModel getById(Long id) {
        return dao.getById(id);
    }

    
    public List<DevConfigModel> getAll() {
        return dao.getAll();
    }


    public  List<DevConfigModel> getPlatformConfigList(int forServer, int forApp,  int page, int pageSize){
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNo(page);
        pageQuery.setPageSize(pageSize);
        return dao.getPlatformConfigList(forServer, forApp, pageQuery);
    }

    public int countPlatformConfigList(int forServer, int forApp){
        return dao.countPlatformConfigList(forServer, forApp);
    }

    public  List<DevConfigModel> getConfigList(int page, int pageSize){
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNo(page);
        pageQuery.setPageSize(pageSize);
        return dao.getConfigList(page, pageSize);
    }

    public int countConfigList(){
        return dao.countConfigList();
    }


    
    public List<DevConfigModel> getByIdList(List<Long> ids) {
        return dao.getByIdList(ids);
    }

    
    public List<DevConfigModel> selectRaw(String sql, Map<String, Object> map) {
        return dao.selectRaw(sql, map);
    }
    public int countRaw(String sql, Map<String, Object> map) {
        return dao.countRaw(sql, map);
    }
}

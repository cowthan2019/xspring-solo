package com.danger.app.device;

import com.danger.DaoFacade;
import org.cp4j.core.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 */
@Service
public class DeviceService {


    @Autowired
    private DeviceDao dao;

    @Autowired
    private DaoFacade facade;

    public DeviceModel selectByPlatformAndDeviceId(String platform,  String deviceId){
        return dao.selectByPlatformAndDeviceId(platform, deviceId);
    }



    public Long insert(DeviceModel e) {
        return dao.insert(e);
    }

    
    public Long insertSelective(DeviceModel e) {
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

    
    public int updateById(DeviceModel e) {
        return dao.updateById(e);
    }

    
    public int update(Long id, Map<String, Object> map) {
        return dao.update(id, map);
    }

    
    public int updateByIdSelective(DeviceModel e) {
        return dao.updateByIdSelective(e);
    }

    
    public int updateWhere(Map<String, Object> contentValues, String where, Map<String, Object> map){
        return dao.updateWhere(contentValues, where, map);
    }

    
    public int count() {
        return dao.count();
    }

    
    public DeviceModel getById(Long id) {
        return dao.getById(id);
    }

    
    public List<DeviceModel> getAll() {
        return dao.getAll();
    }

    
    public List<DeviceModel> getList(int page, int pageSize) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNo(page);
        pageQuery.setPageSize(pageSize);
        return dao.getList(pageQuery);
    }

    
    public List<DeviceModel> getByIdList(List<Long> ids) {
        return dao.getByIdList(ids);
    }

    
    public List<DeviceModel> selectRaw(String sql, Map<String, Object> map) {
        return dao.selectRaw(sql, map);
    }

    
    public int countRaw(String sql, Map<String, Object> map) {
        return dao.countRaw(sql, map);
    }


}
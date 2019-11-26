package com.danger.app.user.service;

import com.danger.DaoFacade;
import com.danger.app.user.dao.OAuth2BindingDao;
import com.danger.app.user.model.OAuth2BindingModel;
import org.cp4j.core.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 */
@Service
public class OAuth2BindingService {

    public OAuth2BindingModel getByPlatformAndOpenId(String platform, String openId){
        return dao.getByPlatformAndOpenId(platform, openId);
    }



    @Autowired
    private OAuth2BindingDao dao;

    @Autowired
    private DaoFacade facade;

    
    public Long insert(OAuth2BindingModel e) {
        return dao.insert(e);
    }

    
    public Long insertSelective(OAuth2BindingModel e) {
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

    
    public int updateById(OAuth2BindingModel e) {
        return dao.updateById(e);
    }

    
    public int update(Long id, Map<String, Object> map) {
        return dao.update(id, map);
    }

    
    public int updateByIdSelective(OAuth2BindingModel e) {
        return dao.updateByIdSelective(e);
    }

    
    public int updateWhere(Map<String, Object> contentValues, String where, Map<String, Object> map){
        return dao.updateWhere(contentValues, where, map);
    }

    
    public int count() {
        return dao.count();
    }

    
    public OAuth2BindingModel getById(Long id) {
        return dao.getById(id);
    }

    
    public List<OAuth2BindingModel> getAll() {
        return dao.getAll();
    }

    
    public List<OAuth2BindingModel> getList(int page, int pageSize) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNo(page);
        pageQuery.setPageSize(pageSize);
        return dao.getList(pageQuery);
    }

    
    public List<OAuth2BindingModel> getByIdList(List<Long> ids) {
        return dao.getByIdList(ids);
    }

    
    public List<OAuth2BindingModel> selectRaw(String sql, Map<String, Object> map) {
        return dao.selectRaw(sql, map);
    }

    
    public int countRaw(String sql, Map<String, Object> map) {
        return dao.countRaw(sql, map);
    }
}
package com.danger.app.user.service;


import com.danger.DaoFacade;
import com.danger.app.user.dao.UserProfileDao;
import com.danger.app.user.model.UserProfileModel;
import org.cp4j.core.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 */
@Service
public class UserProfileService {

    public UserProfileModel getByUserId(Long userId) {
        return dao.getByUserId(userId);
    }


    @Autowired
    private UserProfileDao dao;

    @Autowired
    private DaoFacade facade;


    @Transactional
   
    public Long insert(UserProfileModel e) {
        return dao.insert(e);
    }

   
    public Long insertSelective(UserProfileModel e) {
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

   
    public int updateById(UserProfileModel e) {
        return dao.updateById(e);
    }

   
    public int update(Long id, Map<String, Object> map) {
        return dao.update(id, map);
    }

   
    public int updateByIdSelective(UserProfileModel e) {
        return dao.updateByIdSelective(e);
    }

   
    public int updateWhere(Map<String, Object> contentValues, String where, Map<String, Object> map){
        return dao.updateWhere(contentValues, where, map);
    }

   
    public int count() {
        return dao.count();
    }

   
    public UserProfileModel getById(Long id) {
        return dao.getById(id);
    }

   
    public List<UserProfileModel> getAll() {
        return dao.getAll();
    }

   
    public List<UserProfileModel> getList(int page, int pageSize) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNo(page);
        pageQuery.setPageSize(pageSize);
        return dao.getList(pageQuery);
    }

   
    public List<UserProfileModel> getByIdList(List<Long> ids) {
        return dao.getByIdList(ids);
    }

   
    public List<UserProfileModel> select(String sql, Map<String, Object> map) {
        return dao.select(sql, map);
    }

   
    public List<Map<String, Object>> selectMap(String sql, Map<String, Object> map) {
        return dao.selectMap(sql, map);
    }



   
}

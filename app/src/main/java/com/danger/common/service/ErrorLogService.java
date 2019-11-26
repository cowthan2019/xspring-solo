package com.danger.common.service;


import com.danger.DaoFacade;
import com.danger.common.dao.ErrorLogDao;
import com.danger.common.model.ErrorLogModel;
import org.cp4j.core.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 */
@Service
public class ErrorLogService {
    
    @Autowired
    private ErrorLogDao dao;

    @Autowired
    private DaoFacade facade;

    
    public Long insert(ErrorLogModel e) {
        return dao.insert(e);
    }

    
    public Long insertSelective(ErrorLogModel e) {
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

    
    public int updateById(ErrorLogModel e) {
        return dao.updateById(e);
    }

    
    public int update(Long id, Map<String, Object> map) {
        return dao.update(id, map);
    }

    
    public int updateByIdSelective(ErrorLogModel e) {
        return dao.updateByIdSelective(e);
    }

    
    public int updateWhere(Map<String, Object> contentValues, String where, Map<String, Object> map){
        return dao.updateWhere(contentValues, where, map);
    }

    
    public int count() {
        return dao.count();
    }

    
    public ErrorLogModel getById(Long id) {
        return dao.getById(id);
    }

    
    public List<ErrorLogModel> getAll() {
        return dao.getAll();
    }

    
    public List<ErrorLogModel> getList(int page, int pageSize) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNo(page);
        pageQuery.setPageSize(pageSize);
        return dao.getList(pageQuery);
    }

    
    public List<ErrorLogModel> getByIdList(List<Long> ids) {
        return dao.getByIdList(ids);
    }

    
    public List<ErrorLogModel> select(String sql, Map<String, Object> map) {
        return dao.select(sql, map);
    }

    
    public List<Map<String, Object>> selectMap(String sql, Map<String, Object> map) {
        return dao.selectMap(sql, map);
    }

}

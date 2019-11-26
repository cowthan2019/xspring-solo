package com.danger.app.banner.service;


import com.danger.app.banner.dao.BannerDao;
import com.danger.app.banner.model.BannerModel;
import org.cp4j.core.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * BannerModel ==> model类名
 */
@Service
public class BannerService {


    @Autowired
    private BannerDao dao;


    public List<BannerModel> getByLocation(String location) {
        return dao.getByLocation(location);
    }

    
    public Long insert(BannerModel e) {
        return dao.insert(e);
    }

    
    public Long insertSelective(BannerModel e) {
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

    public int updateById(BannerModel e) {
        return dao.updateById(e);
    }

    
    public int update(Long id, Map<String, Object> map) {
        return dao.update(id, map);
    }

    
    public int updateByIdSelective(BannerModel e) {
        return dao.updateByIdSelective(e);
    }

    
    public int count() {
        return dao.count();
    }




    public BannerModel getById(long id) {
        return dao.getById(id);
    }

    
    public List<BannerModel> getAll() {
        return dao.getAll();
    }

    
    public List<BannerModel> getList(int page, int pageSize) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNo(page);
        pageQuery.setPageSize(pageSize);
        return dao.getList(pageQuery);
    }

    
    public List<BannerModel> getByIdList(List<Long> ids) {
        return dao.getByIdList(ids);
    }


    public List<BannerModel> selectRaw(String sql, Map<String, Object> map) {
        return dao.selectRaw(sql, map);
    }
    public int countRaw(String sql, Map<String, Object> map) {
        return dao.countRaw(sql, map);
    }
}

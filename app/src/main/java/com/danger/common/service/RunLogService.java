package com.danger.common.service;

import com.danger.DaoFacade;
import com.danger.common.dao.RunLogDao;
import com.danger.common.model.RunLogForm;
import com.danger.common.model.RunLogModel;
import com.danger.utils.MyUtils;
import org.cp4j.core.AssocArray;
import org.cp4j.core.JsonUtils;
import org.cp4j.core.PageQuery;
import org.cp4j.core.utils.BeanParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 */
@Service
public class RunLogService {

    public void createOrUpdate(RunLogForm form){
        RunLogModel e = BeanParser.obj_to_obj(RunLogModel.class, form, null);
        if(MyUtils.isValidId(e.getId())){
            updateByIdSelective(e);
        }else{
            insertSelective(e);
        }
    }

    public void addExtra(String mainId, AssocArray extra){
        if(extra == null) return;
        RunLogModel e = getByMainId(mainId);
        if(e == null) return;

        RunLogForm form = BeanParser.obj_to_obj(RunLogForm.class, e, null);
        String bodyJson = e.getBody();
        AssocArray body = JsonUtils.getBean(bodyJson, AssocArray.class);
        body.putAll(extra);
        form.setBody(JsonUtils.toJson(body));

        createOrUpdate(form);
    }

    public RunLogModel getByMainId(String mainId){
        return dao.getByMainId(mainId);
    }

    @Autowired
    private RunLogDao dao;

    @Autowired
    private DaoFacade facade;

    
    public Long insert(RunLogModel e) {
        return dao.insert(e);
    }

    
    public Long insertSelective(RunLogModel e) {
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

    
    public int updateById(RunLogModel e) {
        return dao.updateById(e);
    }

    
    public int update(Long id, Map<String, Object> map) {
        return dao.update(id, map);
    }

    
    public int updateByIdSelective(RunLogModel e) {
        return dao.updateByIdSelective(e);
    }

    
    public int updateWhere(Map<String, Object> contentValues, String where, Map<String, Object> map){
        return dao.updateWhere(contentValues, where, map);
    }

    
    public int count() {
        return dao.count();
    }

    
    public RunLogModel getById(Long id) {
        return dao.getById(id);
    }

    
    public List<RunLogModel> getAll() {
        return dao.getAll();
    }

    
    public List<RunLogModel> getList(int page, int pageSize) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNo(page);
        pageQuery.setPageSize(pageSize);
        return dao.getList(pageQuery);
    }

    
    public List<RunLogModel> getByIdList(List<Long> ids) {
        return dao.getByIdList(ids);
    }

    
    public List<RunLogModel> selectRaw(String sql, Map<String, Object> map) {
        return dao.selectRaw(sql, map);
    }

    
    public int countRaw(String sql, Map<String, Object> map) {
        return dao.countRaw(sql, map);
    }
}
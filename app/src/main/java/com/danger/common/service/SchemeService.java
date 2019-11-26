package com.danger.common.service;


import com.danger.common.dao.SchemeDao;
import org.cp4j.core.AssocArray;
import org.cp4j.core.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 */
@Service
public class SchemeService {

    @Autowired
    SchemeDao dao;

    public List<Map<String, Object>> select(String sql, AssocArray map){
        return dao.select(sql, map);
    }


    public int count(String sql, AssocArray map){
        List<Map<String, Object>> list = dao.select("select count(*) count " + sql, map);
        Map<String, Object> row = list.get(0);
        return Lang.toInt(row.get("count") + "");
    }
}

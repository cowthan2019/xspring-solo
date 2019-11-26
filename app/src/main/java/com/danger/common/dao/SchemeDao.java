package com.danger.common.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cp4j.core.AssocArray;

import java.util.List;
import java.util.Map;

/**
 */
@Mapper
public interface SchemeDao {

    @Select("${sql}")
    List<Map<String, Object>> select(@Param("sql") String sql, @Param("map") Map<String, Object> map);

    @Select("select count(*) ${sql}")
    int count(String sql, AssocArray map);
}

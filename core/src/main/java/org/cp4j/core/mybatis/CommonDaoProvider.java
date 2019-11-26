package org.cp4j.core.mybatis;

import org.cp4j.core.db.MyDbUtils;
import org.cp4j.core.db.Row;

import java.util.Map;

public abstract class CommonDaoProvider<T> {

    public abstract Class<?> findClass();
    public abstract String getTableName();

    public String insert(T e) {
        Row row = MyDbUtils.parse(findClass(), e, false);
        String sql = MyDbUtils.getInsertSqlForMybatis(getTableName(), row, "");
        System.out.println("生成insert语句：" + sql);
        return sql;
    }

    public String insertMap(Map<String, Object> map) {
        Row row = new Row();
        row.putAll(map);
        String sql = MyDbUtils.getInsertSqlForMybatis(getTableName(), row, "map.");
        System.out.println("生成insert语句：" + sql);
        return sql;
    }

    public String insertSelective(T e) {
        Row row = MyDbUtils.parse(findClass(), e, true);
        String sql = MyDbUtils.getInsertSqlForMybatis(getTableName(), row, "");
        System.out.println("生成insert语句,selective：" + sql);
        return sql;
    }

    public String updateById(T e) {
        Row row = MyDbUtils.parse(findClass(), e, false);
        String sql = MyDbUtils.getUpdateSqlForMybatis(getTableName(), row, "");
        System.out.println("生成update语句：" + sql);
        return sql;
    }

    public String updateByIdSelective(T e) {
        Row row = MyDbUtils.parse(findClass(), e, true);
        String sql = MyDbUtils.getUpdateSqlForMybatis(getTableName(), row, "");
        System.out.println("生成update语句, selective：" + sql);
        return sql;
    }

    public String update(Long id, Map<String, Object> map) {
        map.put("id", id); // id必须放到map里，否则拼出来的sql语句找不到#{map.id}
        Row row = new Row();
        row.putAll(map);
        String sql = MyDbUtils.getUpdateSqlForMybatis(getTableName(), row, "map.");
        System.out.println("生成update语句：" + sql);
        return sql;
    }

    public String updateWhere(Map<String, Object> contentValues, String where, Map<String, Object> map) {
        Row row = new Row();
        row.putAll(contentValues);
        String sql = MyDbUtils.getUpdateSqlForMybatis(getTableName(), row, "contentValues.");
        sql = sql.replace( "where id = #{contentValues.id}", "");
        sql += "where " + where;
        System.out.println("生成update语句：" + sql);
        return sql;
    }
}

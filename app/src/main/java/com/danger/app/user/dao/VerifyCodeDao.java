package com.danger.app.user.dao;


import com.danger.app.user.model.VerifyCodeModel;
import org.apache.ibatis.annotations.*;
import org.cp4j.core.db.MyDbUtils;
import org.cp4j.core.db.Row;

import java.util.Date;

@Mapper
public interface VerifyCodeDao {

    class DaoProvider {

        private Row toRow(VerifyCodeModel e, boolean ignoreNull){
            Row row = new Row();
            if(!ignoreNull || e.getId() != null) row.add("id", e.getId());
            if(!ignoreNull || e.getAccount() != null) row.add("account", e.getAccount());
            if(!ignoreNull || e.getCode() != null) row.add("code", e.getCode());
            if(!ignoreNull || e.getStartTime() != null) row.add("start_time", e.getStartTime());
            if(!ignoreNull || e.getGmtCreate() != null) row.add("gmt_create", e.getGmtCreate());
            if(!ignoreNull || e.getGmtModified() != null) row.add("gmt_modified", e.getGmtModified());
            return row;
        }

        private String postProcessSql(String sql){
            sql = sql.replace("{start_time}", "{startTime}");
            sql = sql.replace("{gmt_create}", "{gmtCreate}");
            sql = sql.replace("{gmt_modified}", "{gmtModified}");
            return sql;
        }

        public String insertSelective(VerifyCodeModel e) {
            Row row = toRow(e, true);
            String sql = MyDbUtils.getInsertSqlForMybatis("t_verify_code", row, "");
            sql = postProcessSql(sql);
            System.out.println("生成insert语句,selective：" + sql);
            return sql;
        }
    }


    @InsertProvider(type = DaoProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insertSelective(VerifyCodeModel e);


    @Update("update t_verify_code set code = #{code}, start_time = #{startTime} where account = #{account}")
    int updateCodeByAccount(@Param("account") String account, @Param("code") String code, @Param("startTime") Date startTime);


    @Delete("delete from t_verify_code where account = #{account}")
    int deleteByAccount(@Param("account") String account);

    @Select("select * from t_verify_code where account = #{account}")
    VerifyCodeModel getByAccount(@Param("account") String account);
}

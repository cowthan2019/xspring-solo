package com.danger.common.dao;


import com.danger.common.model.ErrorLogModel;
import org.apache.ibatis.annotations.*;
import org.cp4j.core.PageQuery;
import org.cp4j.core.mybatis.CommonDaoProvider;

import java.util.List;
import java.util.Map;

/**
 */
@Mapper
public interface ErrorLogDao {

    class DaoProvider extends CommonDaoProvider<ErrorLogModel> {
        @Override
        public Class<?> findClass() {
            return ErrorLogModel.class;
        }

        @Override
        public String getTableName() {
            return "t_error_log";
        }
    }

    @InsertProvider(type = DaoProvider.class, method = "insert")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insert(ErrorLogModel e);

    @InsertProvider(type = DaoProvider.class, method = "insertMap")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insertMap(@Param("map") Map<String, Object> map);

    @InsertProvider(type = DaoProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insertSelective(ErrorLogModel e);

    @Delete("delete from t_error_log where id = #{id}")
    int deleteByIdReal(@Param("id") Long id);

    @Delete("update t_error_log set deleted = 1 where id = #{id}")
    int deleteByIdFake(@Param("id") Long id);

    @Delete("delete from t_error_log where ${where}")
    int deleteReal(@Param("where") String where, @Param("map") Map<String, Object> map);

    @Delete("update t_error_log set deleted = 1  where ${where}")
    int deleteFake(@Param("where") String where, @Param("map") Map<String, Object> map);

    @UpdateProvider(type = DaoProvider.class, method = "updateById")
    int updateById(ErrorLogModel e);

    @UpdateProvider(type = DaoProvider.class, method = "updateByIdSelective")
    int updateByIdSelective(ErrorLogModel e);

    @UpdateProvider(type = DaoProvider.class, method = "update")
    int update(@Param("id") Long id, @Param("map") Map<String, Object> map);

    @UpdateProvider(type = DaoProvider.class, method = "updateWhere")
    int updateWhere(@Param("id") Map<String, Object> contentValues, @Param("where") String where, @Param("map") Map<String, Object> map);

    @Select("select count(*) from t_error_log")
    int count();

    @Results({
            @Result(property="id",column="id"),
    })
    @Select("select * from t_error_log where id = #{id}")
    ErrorLogModel getById(@Param("id") Long id);

    @Select("select * from t_error_log")
    List<ErrorLogModel> getAll();

    @Select("select * from t_error_log order by id asc limit #{page.offset}, #{page.pageSize}")
    List<ErrorLogModel> getList(@Param("page") PageQuery page);

    @Select("<script> select * from t_error_log where id in <foreach collection=\"idList\" item=\"id\" index=\"index\" open=\"(\" close=\")\" separator=\",\">#{id}</foreach> </script>")
    List<ErrorLogModel> getByIdList(@Param("idList") List<Long> idList);

    @Select("${sql}")
    List<ErrorLogModel> select(@Param("sql") String sql, @Param("map") Map<String, Object> map);

    @Select("${sql}")
    List<Map<String, Object>> selectMap(@Param("sql") String sql, @Param("map") Map<String, Object> map);

}

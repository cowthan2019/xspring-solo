package com.danger.common.dao;


import com.danger.common.model.RunLogModel;
import org.apache.ibatis.annotations.*;
import org.cp4j.core.PageQuery;
import org.cp4j.core.mybatis.CommonDaoProvider;

import java.util.List;
import java.util.Map;

/**
 */
@Mapper
public interface RunLogDao {

    @Select("select * from t_run_log where mainId = #{mainId}")
    RunLogModel getByMainId(@Param("mainId") String mainId);

    class DaoProvider extends CommonDaoProvider<RunLogModel> {
        @Override
        public Class<?> findClass() {
            return RunLogModel.class;
        }

        @Override
        public String getTableName() {
            return "t_run_log";
        }
    }

    @InsertProvider(type = DaoProvider.class, method = "insert")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insert(RunLogModel e);

    @InsertProvider(type = DaoProvider.class, method = "insertMap")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insertMap(@Param("map") Map<String, Object> map);

    @InsertProvider(type = DaoProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insertSelective(RunLogModel e);

    @Delete("delete from t_run_log where id = #{id}")
    int deleteByIdReal(@Param("id") Long id);

    @Delete("update t_run_log set deleted = 1 where id = #{id}")
    int deleteByIdFake(@Param("id") Long id);

    @Delete("delete from t_run_log where ${where}")
    int deleteReal(@Param("where") String where, @Param("map") Map<String, Object> map);

    @Delete("update t_run_log set deleted = 1  where ${where}")
    int deleteFake(@Param("where") String where, @Param("map") Map<String, Object> map);

    @UpdateProvider(type = DaoProvider.class, method = "updateById")
    int updateById(RunLogModel e);

    @UpdateProvider(type = DaoProvider.class, method = "updateByIdSelective")
    int updateByIdSelective(RunLogModel e);

    @UpdateProvider(type = DaoProvider.class, method = "update")
    int update(@Param("id") Long id, @Param("map") Map<String, Object> map);

    @UpdateProvider(type = DaoProvider.class, method = "updateWhere")
    int updateWhere(@Param("id") Map<String, Object> contentValues, @Param("where") String where, @Param("map") Map<String, Object> map);

    @Select("select count(*) from t_run_log")
    int count();

    @Results({
            @Result(property="id",column="id"),
    })
    @Select("select * from t_run_log where id = #{id}")
    RunLogModel getById(@Param("id") Long id);

    @Select("select * from t_run_log")
    List<RunLogModel> getAll();

    @Select("select * from t_run_log order by id asc limit #{page.offset}, #{page.pageSize}")
    List<RunLogModel> getList(@Param("page") PageQuery page);

    @Select("<script> select * from t_run_log where id in <foreach collection=\"idList\" item=\"id\" index=\"index\" open=\"(\" close=\")\" separator=\",\">#{id}</foreach> </script>")
    List<RunLogModel> getByIdList(@Param("idList") List<Long> idList);


    @Select("${sql}")
    List<RunLogModel> selectRaw(@Param("sql") String sql, @Param("map") Map<String, Object> map);

    @Select("select count(*) ${sql}")
    int countRaw(@Param("sql") String sql, @Param("map") Map<String, Object> map);

}

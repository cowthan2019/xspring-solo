package com.danger.app.devconf;


import org.apache.ibatis.annotations.*;
import org.cp4j.core.PageQuery;
import org.cp4j.core.mybatis.CommonDaoProvider;

import java.util.List;
import java.util.Map;

/**
 */
@Mapper
public interface DevConfigDao {

    class DaoProvider extends CommonDaoProvider<DevConfigModel> {
        @Override
        public Class<?> findClass() {
            return DevConfigModel.class;
        }

        @Override
        public String getTableName() {
            return "t_dev_config";
        }
    }

    @InsertProvider(type = DaoProvider.class, method = "insert")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insert(DevConfigModel e);

    @InsertProvider(type = DaoProvider.class, method = "insertMap")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insertMap(@Param("map") Map<String, Object> map);

    @InsertProvider(type = DaoProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insertSelective(DevConfigModel e);

    @Delete("delete from t_dev_config where id = #{id}")
    int deleteByIdReal(@Param("id") Long id);

    @Delete("update t_dev_config set deleted = 1 where id = #{id}")
    int deleteByIdFake(@Param("id") Long id);

    @Delete("delete from t_dev_config where ${where}")
    int deleteReal(@Param("where") String where, @Param("map") Map<String, Object> map);

    @Delete("update t_dev_config set deleted = 1  where ${where}")
    int deleteFake(@Param("where") String where, @Param("map") Map<String, Object> map);

    @UpdateProvider(type = DaoProvider.class, method = "updateById")
    int updateById(DevConfigModel e);

    @UpdateProvider(type = DaoProvider.class, method = "updateByIdSelective")
    int updateByIdSelective(DevConfigModel e);

    @UpdateProvider(type = DaoProvider.class, method = "update")
    int update(@Param("id") Long id, @Param("map") Map<String, Object> map);

    @UpdateProvider(type = DaoProvider.class, method = "updateWhere")
    int updateWhere(@Param("id") Map<String, Object> contentValues, @Param("where") String where, @Param("map") Map<String, Object> map);

    @Select("select count(*) from t_dev_config")
    int count();

    @Results({
            @Result(property="id",column="id"),
    })
    @Select("select * from t_dev_config where id = #{id}")
    DevConfigModel getById(@Param("id") Long id);

    @Select("select * from t_dev_config")
    List<DevConfigModel> getAll();

    @Select("select * from t_dev_config where for_server = #{forServer} and for_app = #{forApp}  and deleted = 0 order by id asc limit #{page.offset}, #{page.pageSize}")
    List<DevConfigModel> getPlatformConfigList(@Param("forServer") int forServer, @Param("forApp") int forApp, @Param("page") PageQuery page);

    @Select("select count(*) from t_dev_config where for_server = #{forServer} and for_app = #{forApp} and deleted = 0")
    int countPlatformConfigList(@Param("forServer") int forServer, @Param("forApp") int forApp);

    @Select("select * from t_dev_config where  deleted = 0 order by id asc limit #{page}, #{pageSize}")
    List<DevConfigModel> getConfigList(int page, int pageSize);

    @Select("select count(*) from t_dev_config where  deleted = 0")
    int countConfigList();


    @Select("<script> select * from t_dev_config where id in <foreach collection=\"idList\" item=\"id\" index=\"index\" open=\"(\" close=\")\" separator=\",\">#{id}</foreach> </script>")
    List<DevConfigModel> getByIdList(@Param("idList") List<Long> idList);

    @Select("${sql}")
    List<DevConfigModel> selectRaw(@Param("sql") String sql, @Param("map") Map<String, Object> map);

    @Select("select count(*) ${sql}")
    int countRaw(@Param("sql") String sql, @Param("map") Map<String, Object> map);
}

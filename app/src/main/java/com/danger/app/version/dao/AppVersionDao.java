package com.danger.app.version.dao;


import com.danger.app.version.model.AppVersionModel;
import org.apache.ibatis.annotations.*;
import org.cp4j.core.PageQuery;
import org.cp4j.core.mybatis.CommonDaoProvider;

import java.util.List;
import java.util.Map;

@Mapper
public interface AppVersionDao {



    @Results({
            @Result(property="versionCode",column="version_code"),
            @Result(property="changeLog",column="change_log"),
    })
    @Select("select * from t_app_version where platform = #{platform} order by id desc limit 0, 1")
    AppVersionModel getLatestVersion(String platform);


    class DaoProvider extends CommonDaoProvider<AppVersionModel> {
        @Override
        public Class<?> findClass() {
            return AppVersionModel.class;
        }

        @Override
        public String getTableName() {
            return "t_app_version";
        }
    }

    @InsertProvider(type = DaoProvider.class, method = "insert")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insert(AppVersionModel e);

    @InsertProvider(type = DaoProvider.class, method = "insertMap")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insertMap(@Param("map") Map<String, Object> map);

    @InsertProvider(type = DaoProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insertSelective(AppVersionModel e);

    @Delete("delete from t_app_version where id = #{id}")
    int deleteByIdReal(@Param("id") Long id);

    @Delete("update t_app_version set deleted = 1 where id = #{id}")
    int deleteByIdFake(@Param("id") Long id);

    @Delete("delete from t_app_version where ${where}")
    int deleteReal(@Param("where") String where, @Param("map") Map<String, Object> map);

    @Delete("update t_app_version set deleted = 1  where ${where}")
    int deleteFake(@Param("where") String where, @Param("map") Map<String, Object> map);

    @UpdateProvider(type = DaoProvider.class, method = "updateById")
    int updateById(AppVersionModel e);

    @UpdateProvider(type = DaoProvider.class, method = "updateByIdSelective")
    int updateByIdSelective(AppVersionModel e);

    @UpdateProvider(type = DaoProvider.class, method = "update")
    int update(@Param("id") Long id, @Param("map") Map<String, Object> map);

    @UpdateProvider(type = DaoProvider.class, method = "updateWhere")
    int updateWhere(@Param("id") Map<String, Object> contentValues, @Param("where") String where, @Param("map") Map<String, Object> map);

    @Select("select count(*) from t_app_version")
    int count();

    @Results({
            @Result(property="id",column="id"),
    })
    @Select("select * from t_app_version where id = #{id}")
    AppVersionModel getById(@Param("id") Long id);

    @Select("select * from t_app_version")
    List<AppVersionModel> getAll();

    @Select("select * from t_app_version order by id asc limit #{page.offset}, #{page.pageSize}")
    List<AppVersionModel> getList(@Param("page") PageQuery page);

    @Select("<script> select * from t_app_version where id in <foreach collection=\"idList\" item=\"id\" index=\"index\" open=\"(\" close=\")\" separator=\",\">#{id}</foreach> </script>")
    List<AppVersionModel> getByIdList(@Param("idList") List<Long> idList);


    @Select("${sql}")
    List<AppVersionModel> selectRaw(@Param("sql") String sql, @Param("map") Map<String, Object> map);

    @Select("select count(*) ${sql}")
    int countRaw(@Param("sql") String sql, @Param("map") Map<String, Object> map);


}

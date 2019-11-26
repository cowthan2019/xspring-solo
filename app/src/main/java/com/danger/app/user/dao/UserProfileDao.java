package com.danger.app.user.dao;


import com.danger.app.user.model.UserProfileModel;
import org.apache.ibatis.annotations.*;
import org.cp4j.core.PageQuery;
import org.cp4j.core.mybatis.CommonDaoProvider;

import java.util.List;
import java.util.Map;

/**
 */
@Mapper
public interface UserProfileDao {

    class DaoProvider extends CommonDaoProvider<UserProfileModel> {
        @Override
        public Class<?> findClass() {
            return UserProfileModel.class;
        }

        @Override
        public String getTableName() {
            return "t_user_profile";
        }
    }

    @InsertProvider(type = DaoProvider.class, method = "insert")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insert(UserProfileModel e);

    @InsertProvider(type = DaoProvider.class, method = "insertMap")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insertMap(@Param("map") Map<String, Object> map);

    @InsertProvider(type = DaoProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insertSelective(UserProfileModel e);

    @Delete("delete from t_user_profile where id = #{id}")
    int deleteByIdReal(@Param("id") Long id);

    @Delete("update t_user_profile set deleted = 1 where id = #{id}")
    int deleteByIdFake(@Param("id") Long id);

    @Delete("delete from t_user_profile where ${where}")
    int deleteReal(@Param("where") String where, @Param("map") Map<String, Object> map);

    @Delete("update t_user_profile set deleted = 1  where ${where}")
    int deleteFake(@Param("where") String where, @Param("map") Map<String, Object> map);

    @UpdateProvider(type = DaoProvider.class, method = "updateById")
    int updateById(UserProfileModel e);

    @UpdateProvider(type = DaoProvider.class, method = "updateByIdSelective")
    int updateByIdSelective(UserProfileModel e);

    @UpdateProvider(type = DaoProvider.class, method = "update")
    int update(@Param("id") Long id, @Param("map") Map<String, Object> map);

    @UpdateProvider(type = DaoProvider.class, method = "updateWhere")
    int updateWhere(@Param("contentValues") Map<String, Object> contentValues, @Param("where") String where, @Param("map") Map<String, Object> map);

    @Select("select count(*) from t_user_profile")
    int count();

    @Results({
            @Result(property="id",column="id"),
    })
    @Select("select * from t_user_profile where id = #{id}")
    UserProfileModel getById(@Param("id") Long id);

    @Select("select * from t_user_profile")
    List<UserProfileModel> getAll();

    @Select("select * from t_user_profile order by id asc limit #{page.offset}, #{page.pageSize}")
    List<UserProfileModel> getList(@Param("page") PageQuery page);

    @Select("<script> select * from t_user_profile where id in <foreach collection=\"idList\" item=\"id\" index=\"index\" open=\"(\" close=\")\" separator=\",\">#{id}</foreach> </script>")
    List<UserProfileModel> getByIdList(@Param("idList") List<Long> idList);

    @Select("${sql}")
    List<UserProfileModel> select(@Param("sql") String sql, @Param("map") Map<String, Object> map);

    @Select("${sql}")
    List<Map<String, Object>> selectMap(@Param("sql") String sql, @Param("map") Map<String, Object> map);


    @Select("select * from t_user_profile where user_id = #{userId}")
    UserProfileModel getByUserId(@Param("userId") Long userId);


}

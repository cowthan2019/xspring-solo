package com.danger.app.user.dao;


import com.danger.app.user.model.AuthModel;
import org.apache.ibatis.annotations.*;
import org.cp4j.core.PageQuery;
import org.cp4j.core.mybatis.CommonDaoProvider;

import java.util.List;
import java.util.Map;

/**
 */
@Mapper
public interface AuthDao {

    @Select("select * from t_auth where username = #{username} and deleted = 0")
    AuthModel getByUsername(String username);

    @Select("select * from t_auth where uid = #{uid}")
    AuthModel getByUid(String uid);

    @Select("select count(*) from t_auth where sid = #{sid}")
    int countBySid(String sid);

    class DaoProvider extends CommonDaoProvider<AuthModel> {
        @Override
        public Class<?> findClass() {
            return AuthModel.class;
        }

        @Override
        public String getTableName() {
            return "t_auth";
        }
    }

    @InsertProvider(type = DaoProvider.class, method = "insert")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insert(AuthModel e);

    @InsertProvider(type = DaoProvider.class, method = "insertMap")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insertMap(@Param("map") Map<String, Object> map);

    @InsertProvider(type = DaoProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insertSelective(AuthModel e);

    @Delete("delete from t_auth where id = #{id}")
    int deleteByIdReal(@Param("id") Long id);

    @Delete("update t_auth set deleted = 1 where id = #{id}")
    int deleteByIdFake(@Param("id") Long id);

    @Delete("delete from t_auth where ${where}")
    int deleteReal(@Param("where") String where, @Param("map") Map<String, Object> map);

    @Delete("update t_auth set deleted = 1  where ${where}")
    int deleteFake(@Param("where") String where, @Param("map") Map<String, Object> map);

    @UpdateProvider(type = DaoProvider.class, method = "updateById")
    int updateById(AuthModel e);

    @UpdateProvider(type = DaoProvider.class, method = "updateByIdSelective")
    int updateByIdSelective(AuthModel e);

    @UpdateProvider(type = DaoProvider.class, method = "update")
    int update(@Param("id") Long id, @Param("map") Map<String, Object> map);

    @UpdateProvider(type = DaoProvider.class, method = "updateWhere")
    int updateWhere(@Param("id") Map<String, Object> contentValues, @Param("where") String where, @Param("map") Map<String, Object> map);

    @Select("select count(*) from t_auth")
    int count();

    @Results({
            @Result(property="id",column="id"),
    })
    @Select("select * from t_auth where id = #{id}")
    AuthModel getById(@Param("id") Long id);

    @Select("select * from t_auth")
    List<AuthModel> getAll();

    @Select("select * from t_auth order by id asc limit #{page.offset}, #{page.pageSize}")
    List<AuthModel> getList(@Param("page") PageQuery page);

    @Select("<script> select * from t_auth where id in <foreach collection=\"idList\" item=\"id\" index=\"index\" open=\"(\" close=\")\" separator=\",\">#{id}</foreach> </script>")
    List<AuthModel> getByIdList(@Param("idList") List<Long> idList);


    @Select("${sql}")
    List<AuthModel> selectRaw(@Param("sql") String sql, @Param("map") Map<String, Object> map);

    @Select("select count(*) ${sql}")
    int countRaw(@Param("sql") String sql, @Param("map") Map<String, Object> map);

}

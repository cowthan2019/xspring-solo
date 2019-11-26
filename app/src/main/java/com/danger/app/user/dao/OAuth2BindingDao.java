package com.danger.app.user.dao;


import com.danger.app.user.model.OAuth2BindingModel;
import org.apache.ibatis.annotations.*;
import org.cp4j.core.PageQuery;
import org.cp4j.core.mybatis.CommonDaoProvider;

import java.util.List;
import java.util.Map;

/**
 */
@Mapper
public interface OAuth2BindingDao {


    @Select("select * from t_oauth2_binding where platform = #{platform} and open_id = #{openId}")
    OAuth2BindingModel getByPlatformAndOpenId(@Param("platform") String platform, @Param("openId") String openId);


    class DaoProvider extends CommonDaoProvider<OAuth2BindingModel> {
        @Override
        public Class<?> findClass() {
            return OAuth2BindingModel.class;
        }

        @Override
        public String getTableName() {
            return "t_oauth2_binding";
        }
    }

    @InsertProvider(type = DaoProvider.class, method = "insert")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insert(OAuth2BindingModel e);

    @InsertProvider(type = DaoProvider.class, method = "insertMap")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insertMap(@Param("map") Map<String, Object> map);

    @InsertProvider(type = DaoProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insertSelective(OAuth2BindingModel e);

    @Delete("delete from t_oauth2_binding where id = #{id}")
    int deleteByIdReal(@Param("id") Long id);

    @Delete("update t_oauth2_binding set deleted = 1 where id = #{id}")
    int deleteByIdFake(@Param("id") Long id);

    @Delete("delete from t_oauth2_binding where ${where}")
    int deleteReal(@Param("where") String where, @Param("map") Map<String, Object> map);

    @Delete("update t_oauth2_binding set deleted = 1  where ${where}")
    int deleteFake(@Param("where") String where, @Param("map") Map<String, Object> map);

    @UpdateProvider(type = DaoProvider.class, method = "updateById")
    int updateById(OAuth2BindingModel e);

    @UpdateProvider(type = DaoProvider.class, method = "updateByIdSelective")
    int updateByIdSelective(OAuth2BindingModel e);

    @UpdateProvider(type = DaoProvider.class, method = "update")
    int update(@Param("id") Long id, @Param("map") Map<String, Object> map);

    @UpdateProvider(type = DaoProvider.class, method = "updateWhere")
    int updateWhere(@Param("id") Map<String, Object> contentValues, @Param("where") String where, @Param("map") Map<String, Object> map);

    @Select("select count(*) from t_oauth2_binding")
    int count();

    @Results({
            @Result(property="id",column="id"),
    })
    @Select("select * from t_oauth2_binding where id = #{id}")
    OAuth2BindingModel getById(@Param("id") Long id);

    @Select("select * from t_oauth2_binding")
    List<OAuth2BindingModel> getAll();

    @Select("select * from t_oauth2_binding order by id asc limit #{page.offset}, #{page.pageSize}")
    List<OAuth2BindingModel> getList(@Param("page") PageQuery page);

    @Select("<script> select * from t_oauth2_binding where id in <foreach collection=\"idList\" item=\"id\" index=\"index\" open=\"(\" close=\")\" separator=\",\">#{id}</foreach> </script>")
    List<OAuth2BindingModel> getByIdList(@Param("idList") List<Long> idList);


    @Select("${sql}")
    List<OAuth2BindingModel> selectRaw(@Param("sql") String sql, @Param("map") Map<String, Object> map);

    @Select("select count(*) ${sql}")
    int countRaw(@Param("sql") String sql, @Param("map") Map<String, Object> map);

}

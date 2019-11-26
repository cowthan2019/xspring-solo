package com.danger.app.banner.dao;


import com.danger.app.banner.model.BannerModel;
import org.apache.ibatis.annotations.*;
import org.cp4j.core.PageQuery;
import org.cp4j.core.mybatis.CommonDaoProvider;

import java.util.List;
import java.util.Map;

/**
 * BannerModel ==> 类名
 * t_banner ==> 表名
 */
@Mapper
public interface BannerDao {

    @Select("select * from t_banner where location = #{location} and deleted != 1 and status = 0 order by sort asc")
    List<BannerModel> getByLocation(String location);

    @Select("select * from t_banner where id = #{id}")
    BannerModel getById(long id);

    class DaoProvider extends CommonDaoProvider<BannerModel> {
        @Override
        public Class<?> findClass() {
            return BannerModel.class;
        }

        @Override
        public String getTableName() {
            return "t_banner";
        }
    }

    @InsertProvider(type = DaoProvider.class, method = "insert")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insert(BannerModel e);

    @InsertProvider(type = DaoProvider.class, method = "insertMap")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insertMap(@Param("map") Map<String, Object> map);

    @InsertProvider(type = DaoProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insertSelective(BannerModel e);

    @Delete("delete from t_banner where id = #{id}")
    int deleteByIdReal(@Param("id") Long id);

    @Delete("update t_banner set deleted = 1 where id = #{id}")
    int deleteByIdFake(@Param("id") Long id);

    @Delete("delete from t_banner where ${where}")
    int deleteReal(@Param("where") String where, @Param("map") Map<String, Object> map);

    @Delete("update t_banner set deleted = 1  where ${where}")
    int deleteFake(@Param("where") String where, @Param("map") Map<String, Object> map);


    @UpdateProvider(type = DaoProvider.class, method = "updateById")
    int updateById(BannerModel e);

    @UpdateProvider(type = DaoProvider.class, method = "updateByIdSelective")
    int updateByIdSelective(BannerModel e);

    @UpdateProvider(type = DaoProvider.class, method = "update")
    int update(@Param("id") Long id, @Param("map") Map<String, Object> map);

    @Update("update t_banner set status = #{status} where id = #{id}")
    int updateStatusById(@Param("id") Long id, @Param("status") Integer status);

    @Select("select count(*) from t_banner")
    int count();

    @Results({
            @Result(property="id",column="id"),
    })

    @Select("select * from t_banner")
    List<BannerModel> getAll();

    @Select("select * from t_banner order by id asc limit #{page.offset}, #{page.pageSize}")
    List<BannerModel> getList(@Param("page") PageQuery page);

    @Select("<script> select * from t_banner where id in <foreach collection=\"idList\" item=\"id\" index=\"index\" open=\"(\" close=\")\" separator=\",\">#{id}</foreach> </script>")
    List<BannerModel> getByIdList(@Param("idList") List<Long> idList);

    @Select("${sql}")
    List<BannerModel> selectRaw(@Param("sql") String sql, @Param("map") Map<String, Object> map);

    @Select("select count(*) ${sql}")
    int countRaw(@Param("sql") String sql, @Param("map") Map<String, Object> map);



}

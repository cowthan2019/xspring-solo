package com.danger.app.device;


import org.apache.ibatis.annotations.*;
import org.cp4j.core.PageQuery;
import org.cp4j.core.mybatis.CommonDaoProvider;

import java.util.List;
import java.util.Map;

/**
 */
@Mapper
public interface DeviceDao {


    @Select("select * from t_device where platform = #{platform} and device_id = #{deviceId}")
    DeviceModel selectByPlatformAndDeviceId(@Param("platform") String platform, @Param("deviceId") String deviceId);


    class DaoProvider extends CommonDaoProvider<DeviceModel> {
        @Override
        public Class<?> findClass() {
            return DeviceModel.class;
        }

        @Override
        public String getTableName() {
            return "t_device";
        }
    }

    @InsertProvider(type = DaoProvider.class, method = "insert")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insert(DeviceModel e);

    @InsertProvider(type = DaoProvider.class, method = "insertMap")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insertMap(@Param("map") Map<String, Object> map);

    @InsertProvider(type = DaoProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long insertSelective(DeviceModel e);

    @Delete("delete from t_device where id = #{id}")
    int deleteByIdReal(@Param("id") Long id);

    @Delete("update t_device set deleted = 1 where id = #{id}")
    int deleteByIdFake(@Param("id") Long id);

    @Delete("delete from t_device where ${where}")
    int deleteReal(@Param("where") String where, @Param("map") Map<String, Object> map);

    @Delete("update t_device set deleted = 1  where ${where}")
    int deleteFake(@Param("where") String where, @Param("map") Map<String, Object> map);

    @UpdateProvider(type = DaoProvider.class, method = "updateById")
    int updateById(DeviceModel e);

    @UpdateProvider(type = DaoProvider.class, method = "updateByIdSelective")
    int updateByIdSelective(DeviceModel e);

    @UpdateProvider(type = DaoProvider.class, method = "update")
    int update(@Param("id") Long id, @Param("map") Map<String, Object> map);

    @UpdateProvider(type = DaoProvider.class, method = "updateWhere")
    int updateWhere(@Param("id") Map<String, Object> contentValues, @Param("where") String where, @Param("map") Map<String, Object> map);

    @Select("select count(*) from t_device")
    int count();

    @Results({
            @Result(property="id",column="id"),
    })
    @Select("select * from t_device where id = #{id}")
    DeviceModel getById(@Param("id") Long id);

    @Select("select * from t_device")
    List<DeviceModel> getAll();

    @Select("select * from t_device order by id asc limit #{page.offset}, #{page.pageSize}")
    List<DeviceModel> getList(@Param("page") PageQuery page);

    @Select("<script> select * from t_device where id in <foreach collection=\"idList\" item=\"id\" index=\"index\" open=\"(\" close=\")\" separator=\",\">#{id}</foreach> </script>")
    List<DeviceModel> getByIdList(@Param("idList") List<Long> idList);


    @Select("${sql}")
    List<DeviceModel> selectRaw(@Param("sql") String sql, @Param("map") Map<String, Object> map);

    @Select("select count(*) ${sql}")
    int countRaw(@Param("sql") String sql, @Param("map") Map<String, Object> map);

}

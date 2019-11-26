package org.cp4j.core.db;

import org.cp4j.core.AssocArray;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class MyDb {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public MyDb(AssocArray config) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//        String uri = "jdbc:mysql://{host}:{port}/{dbname}?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false";
        String uri = "jdbc:mysql://{host}:{port}/{dbname}?characterEncoding=utf-8&useUnicode=true&allowMultiQueries=true&useSSL=false";
        uri = uri.replace("{host}", config.getString("host", ""));
        uri = uri.replace("{port}", config.getString("port", ""));
        uri = uri.replace("{dbname}", config.getString("dbname", ""));
        dataSource.setUrl(uri);
        dataSource.setUsername(config.getString("username", ""));
        dataSource.setPassword(config.getString("password", ""));

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        checkDbConnection();
    }

    public void checkDbConnection() {
//        String sql = "SELECT 1 FROM DUAL";
//        findRow(sql, null);
    }

    public int insert(String sql, Params params){
        return jdbcTemplate.update(sql, params);
    }

    public long insertAndGetLastId(String sql, Params params, String idColumnName){
        KeyHolder keyHolder=new GeneratedKeyHolder();
        SqlParameterSource sps = new MapSqlParameterSource();
        ((MapSqlParameterSource) sps).addValues(params);
        jdbcTemplate.update(sql, sps, keyHolder, new String[]{idColumnName});
        return keyHolder.getKey().longValue();
    }

//    public void insert(String table, List<Row> list){
//        for (int i = 0; i < Lang.count(list); i++) {
//            Object[] info = MyDbUtils.getInsertSql(table, list.get(i));
//            String sql = (String) info[0];
//            Params pm = (Params) info[1];
//
//        }
//    }

    public int update(String sql, Params params){
        return jdbcTemplate.update(sql, params);
    }

    public int[] batchUpdate(String sql, Params[] params){
        return jdbcTemplate.batchUpdate(sql,params);
    }

    public int delete(String sql, Params params){
        return jdbcTemplate.update(sql, params);
    }

    public List<Row> findAll(String sql, Params params){
        List<Row> rows = jdbcTemplate.query(sql, params, new GenericMapper());
        return rows;
    }

    public Row findRow(String sql, Params params){
        List<Row> rows = jdbcTemplate.query(sql, params, new GenericMapper());
        if(rows != null && rows.size() > 0){
            return rows.get(0);
        }else{
            return null;
        }
    }

    public int findColumn(String sql, Params params, int defaultValue){
        List<Integer> rows = jdbcTemplate.query(sql, params, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt(1);
            }
        });
        if(rows != null && rows.size() > 0){
            return rows.get(0) != null ? rows.get(0) : defaultValue;
        }else{
            return defaultValue;
        }
    }

    public String findColumn(String sql, Params params, String defaultValue){
        List<String> rows = jdbcTemplate.query(sql, params, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString(1);
            }
        });
        if(rows != null && rows.size() > 0){
            return rows.get(0) != null ? rows.get(0) : defaultValue;
        }else{
            return defaultValue;
        }
    }

    public Date findColumnDate(String sql, Params params){
        List<Date> rows = jdbcTemplate.query(sql, params, new RowMapper<Date>() {
            @Override
            public Date mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getTimestamp(1);
            }
        });
        if(rows != null && rows.size() > 0){
            return rows.get(0) != null ? rows.get(0) : null;
        }else{
            return null;
        }
    }

    public double findColumn(String sql, Params params, double defaultValue){
        List<Double> rows = jdbcTemplate.query(sql, params, new RowMapper<Double>() {
            @Override
            public Double mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getDouble(1);
            }
        });
        if(rows != null && rows.size() > 0){
            return rows.get(0) != null ? rows.get(0) : defaultValue;
        }else{
            return defaultValue;
        }
    }

    public Date findColumn(String sql, Params params, Date defaultValue){
        List<Timestamp> rows = jdbcTemplate.query(sql, params, new RowMapper<Timestamp>() {
            @Override
            public Timestamp mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getTimestamp(1);
            }
        });
        if(rows != null && rows.size() > 0){
            return rows.get(0) != null ? rows.get(0) : defaultValue;
        }else{
            return defaultValue;
        }
    }

    public static void test(String[] args){



//        MyDb.getDefault().insert("insert into test " +
//                "set id = :id, " +
//                "name = :name, " +
//                "user_id = :user_id, " +
//                "create_time = :create_time, " +
//                "price = :price;",
//                Params.obtain()
//                        .add("id", System.currentTimeMillis())
//                        .add("name", "name")
//                        .add("user_id", 3)
//                .add("create_time", new Date())
//                .add("price", 234.32)
//        );
//
//        MyDb.getDefault().update("update test set name = :name where id = :id",
//                Params.obtain().add("name", "王二").add("id", "112233"));
//
//        MyDb.getDefault().delete("delete from test where id=:id", Params.obtain().add("id", "112233"));
//
//        List<Row> rows = MyDb.getDefault().findAll("select * from test", null);
//        for(Row row: rows){
//            for(String key: row.keySet()){
//                System.out.println(key + "==>" + row.getObject(key, null)
//                        + "[" + row.getObject(key, null).getClass() + "]");
//
//            }
//            System.out.println("------");
//        }
//
//        System.out.println("==============");
//        Date date = MyDb.getDefault().findColumn("select create_time from test where id='1528217713300'", null, new Date());
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));


        Row row = new Row();
        row.put("name", "老王");
        row.put("birth", new Date());
        row.put("id", 23);

        Object[] info = MyDbUtils.getInsertSql("stu", row);
        String sql = (String) info[0];
        Params pm = (Params) info[1];
        System.out.println(sql);
        System.out.println(pm);
    }


}

package org.cp4j.core.db;

import org.cp4j.core.Lang;
import org.cp4j.core.utils.NameUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

public class MyDbUtils {

    public static boolean isValidDbType(Class<?> clazz){
        if(clazz == Short.class) return true;
        if(clazz == Integer.class) return true;
        if(clazz == Long.class) return true;
        if(clazz == String.class) return true;
        if(clazz == Date.class) return true;

        return false;
    }

    public static Object[] getInsertSql(String table, Row row){
        StringBuilder sb = new StringBuilder();
        Params pm = Params.obtain();

        sb.append("insert into `");
        sb.append(table);
        sb.append("` (");
//        sb.append("values (values_cotent)");

        StringBuilder sbFields = new StringBuilder();
        StringBuilder sbValues = new StringBuilder();

        int loop = 0;
        int totalCount = row.size();
        for (Map.Entry<String, Object> entry: row.entrySet()){
            loop++;
            sbFields.append("`" + entry.getKey() + "`");

            sbValues.append(":");
            sbValues.append(entry.getKey());

            if(loop != totalCount){
                sbFields.append(", ");
                sbValues.append(", ");
            }

            pm.add(entry.getKey(), entry.getValue());
        }

        sb.append(sbFields.toString());
        sb.append(") ");
        sb.append("values (");
        sb.append(sbValues.toString());
        sb.append(")");

        return new Object[]{sb.toString(), pm};
    }

    public static String getInsertSqlForMybatis(String table, Row row, String prefix){
        StringBuilder sb = new StringBuilder();

        sb.append("insert into `");
        sb.append(table);
        sb.append("` (");
//        sb.append("values (values_cotent)");

        StringBuilder sbFields = new StringBuilder();
        StringBuilder sbValues = new StringBuilder();

        int loop = 0;
        int totalCount = row.size();
        for (Map.Entry<String, Object> entry: row.entrySet()){
            loop++;
            sbFields.append("`" + NameUtils.humpToLine2(entry.getKey()) + "`");

            sbValues.append("#{");
            String k = prefix + entry.getKey();
            sbValues.append(k);
            sbValues.append("}");

            if(loop != totalCount){
                sbFields.append(", ");
                sbValues.append(", ");
            }

        }

        sb.append(sbFields.toString());
        sb.append(") ");
        sb.append("values (");
        sb.append(sbValues.toString());
        sb.append(")");

        return sb.toString();
    }


    public static Object[] getUpdateSql(String table, Row row){
        StringBuilder sb = new StringBuilder();
        Params pm = Params.obtain();

        sb.append("update ");
        sb.append(table);
        sb.append(" set ");

        int loop = 0;
        int totalCount = row.size();

        for (Map.Entry<String, Object> entry: row.entrySet()){
            loop++;
//            if("id".equals(entry.getKey())) {
//                continue;
//            }else{
//                sb.append("`" + entry.getKey() + "` = :" + entry.getKey());
//                if(loop != totalCount){
//                    sb.append(", ");
//                }
//                pm.add(entry.getKey(), entry.getValue());
//            }

            sb.append("`" + entry.getKey() + "` = :" + entry.getKey());
            if(loop != totalCount){
                sb.append(", ");
            }
            pm.add(entry.getKey(), entry.getValue());

            //去除id的话，如果id在最后一个，会出现set系列以逗号结尾的情况，不用纠结了，直接把id也update一下行了
        }

        sb.append(" where id = :id");
        pm.add("id", row.getObject("id", null));

        return new Object[]{sb.toString(), pm};
    }


    public static String getUpdateSqlForMybatis(String table, Row row, String prefix){
        if(prefix == null) prefix = "";
        StringBuilder sb = new StringBuilder();
        Params pm = Params.obtain();

        sb.append("update ");
        sb.append(table);
        sb.append(" set ");

        int loop = 0;
        int totalCount = row.size();

        for (Map.Entry<String, Object> entry: row.entrySet()){
            loop++;
//            if("id".equals(entry.getKey())) {
//                continue;
//            }else{
//                sb.append("`" + entry.getKey() + "` = :" + entry.getKey());
//                if(loop != totalCount){
//                    sb.append(", ");
//                }
//                pm.add(entry.getKey(), entry.getValue());
//            }

            sb.append("`" + NameUtils.humpToLine2(entry.getKey()) + "` = #{" + prefix + entry.getKey() + "}");
            if(loop != totalCount){
                sb.append(", ");
            }
            pm.add(entry.getKey(), entry.getValue());

            //去除id的话，如果id在最后一个，会出现set系列以逗号结尾的情况，不用纠结了，直接把id也update一下行了
        }

        sb.append(" where id = #{" + prefix + "id}");
        pm.add("id", row.getObject("id", null));

        return sb.toString();
    }

    public static Row parse(Class<?> clazz, Object o, boolean ignoreNull){
        if(o == null) return null;
        Row row = new Row();

        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < Lang.count(fields); i++) {
            Field f = fields[i];
            f.setAccessible(true);
            if(!isValidDbType(f.getType())) continue;
            if(f.getAnnotation(DbIgnore.class) != null) continue;
            String key = f.getName();
            try {
                Object value = f.get(o);
                if(ignoreNull && value == null) continue;
                row.add(key, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }
        }

        return row;
    }



}

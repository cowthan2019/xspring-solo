package org.cp4j.core.sqlgenerator;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 生成sql语句，使用方式是命令行，结果就是mybatis的xml文件，或者可以选择直接输出纯sql语句
 */
public class MySqlGenerator {

    private static String getColumnNameFromField(Field field){

        String columnName = "";

        //看是不是ID
        MG_ID annotation = field.getAnnotation(MG_ID.class);
        if(annotation != null){
            columnName = annotation.column();
        }

        //看是不是@MG_COLUMN列
        if(columnName == null || "".equals(columnName)){
            MG_COLUMN anno = field.getAnnotation(MG_COLUMN.class);
            if(anno != null){
                columnName = anno.column();
            }
        }

        if(columnName == null || "".equals(columnName)){
            columnName = field.getName();
        }

        if(columnName == null || "".equals(columnName)){
            throw new RuntimeException("无法获取列名");
        }

        return columnName;
    }

    private static String getColumnType(Field f){
        if(f.getType() == Integer.class || f.getType() == int.class){
            return "INTEGER";
        }else if(f.getType() == Long.class || f.getType() == long.class){
            return "LONG";
        }else if(f.getType() == String.class){
            return "VARCHAR";
        }else if(f.getType() == Date.class){
            return "TIMESTAMP";
        }else{
            return "VARCHAR";
        }
    }

    public static ColumnInfo getColumn(Field f){
        MG_ID annotation = f.getAnnotation(MG_ID.class);
        if(annotation != null){
            IdColumnInfo info = new IdColumnInfo();
            info.fieldName = f.getName();
            info.autoIncrement = annotation.autoIncrement();
            info.columnName = getColumnNameFromField(f);
            info.columnType = getColumnType(f);
            info.fieldType = f.getType().getName();
            return info;
        }else{
            ColumnInfo info = new ColumnInfo();
            info.fieldName = f.getName();
            info.columnName = getColumnNameFromField(f);
            info.columnType = getColumnType(f);
            info.fieldType = f.getType().getName();
            return info;
        }
    }

    public static IdColumnInfo getIdColumn(Class<?> clazz){

        Field[] fs = clazz.getDeclaredFields();
        for (Field f: fs){
            ColumnInfo columnInfo = getColumn(f);
            if(columnInfo instanceof IdColumnInfo){
                return (IdColumnInfo)columnInfo;
            }
        }
        throw new RuntimeException("没有ID列");
    }

    public static List<ColumnInfo> getColumnList(Class<?> clazz){
        List<ColumnInfo> list = new ArrayList<ColumnInfo>();

        Field[] fs = clazz.getDeclaredFields();
        boolean hasId = false;
        for (Field f: fs){
            ColumnInfo columnInfo = getColumn(f);
            list.add(columnInfo);
            if(columnInfo instanceof IdColumnInfo){
                hasId = true;
            }
        }
        if(!hasId){
            throw new RuntimeException("没有ID列");
        }
        return list;
    }

    public static String generateBatisXmlHeader(Class<?> clazz){
        String template = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n" +
                "<mapper namespace=\"【--- mapper class path ---】\" >\n" +
                "    <resultMap id=\"{{class_name}}\" type=\"{{class_full_name}}\" >\n" +
                "       {{columns}}" +
                "    </resultMap>\n" +
                "    <sql id=\"Base_Column_List\" >\n" +
                "        {{column_names}} \n" +
                "    </sql>";
        String className = clazz.getSimpleName();
        String classFullName = clazz.getName();
        StringBuilder sbItems = new StringBuilder();
        StringBuilder sbNames = new StringBuilder();
        List<ColumnInfo> columns = getColumnList(clazz);

        for (int i = 0; i < columns.size(); i++) {
            ColumnInfo column = columns.get(i);
            sbItems.append(generateResultMapItem(column));
            sbNames.append(column.columnName);

            if(i != columns.size() - 1){
                sbItems.append("\n        ");
                sbNames.append(", ");
            }
        }
        template = template.replace("{{class_name}}", className);
        template = template.replace("{{class_full_name}}", classFullName);
        template = template.replace("{{columns}}", sbItems.toString());
        template = template.replace("{{column_names}}", sbNames.toString());


        return template;
    }

    public static String generateResultMapItem(ColumnInfo columnInfo){
        if(columnInfo instanceof IdColumnInfo){
            String template = "<id column=\"{{column}}\" property=\"{{property}}\" jdbcType=\"{{type}}\" />";
            return template.replace("{{column}}", columnInfo.columnName)
                        .replace("{{property}}", columnInfo.fieldName)
                        .replace("{{type}}", columnInfo.columnType);
        }else{
            String template = "<result column=\"{{column}}\" property=\"{{property}}\" jdbcType=\"{{type}}\" />";
            return template.replace("{{column}}", columnInfo.columnName)
                    .replace("{{property}}", columnInfo.fieldName)
                    .replace("{{type}}", columnInfo.columnType);
        }
    }

    private static String getInsertColumnItem(ColumnInfo columnInfo, boolean ignoreNull){
        String str = "<if test=\"{{name}} != null\" >{{field}},</if>";
        if(!ignoreNull){
             str = "{{name}}, ";
        }
        str = str.replace("{{name}}", columnInfo.columnName);
        str = str.replace("{{field}}", columnInfo.fieldName);
        return str;
    }

    private static String getInsertValueItem(ColumnInfo columnInfo, boolean ignoreNull){
        String str = "<if test=\"{{name}} != null\" >#{ {{field}},jdbcType={{type}} },</if>";
        if(!ignoreNull){
            str = "#{ {{name}},jdbcType={{type}} }, ";
        }
        str = str.replace("{{name}}", columnInfo.columnName);
        str = str.replace("{{field}}", columnInfo.fieldName);
        str = str.replace("{{type}}", columnInfo.columnType);
        return str;
    }

    private static String getTableName(Class<?> clazz){
        MG_TABLE annotation = clazz.getAnnotation(MG_TABLE.class);
        if(annotation != null){
            return annotation.name();
        }
        return clazz.getSimpleName();
    }

    public static String generateInsertSql(Class<?> clazz, boolean ignoreNull){

        List<ColumnInfo> columns = getColumnList(clazz);
        String culumnXml = "";
        String valueXmls = "";
        String idFiledName = "";
        boolean isAuto = false;
        for (ColumnInfo column: columns){
            if(column instanceof IdColumnInfo){
                idFiledName = column.fieldName;
                if(((IdColumnInfo) column).autoIncrement){
                    continue;
                }
            }
            culumnXml += getInsertColumnItem(column, ignoreNull) + "\n";
            valueXmls += getInsertValueItem(column, ignoreNull) + "\n";

        }

        String header = "<insert id=\"insertSelective\" parameterType=\"header\" keyProperty=\"id\" useGeneratedKeys=\"true\">\n";
        if(!isAuto){
            header = "<insert id=\"insertSelective\" parameterType=\"{{class_full_name}}\" keyProperty=\"{{id_field}}\" >\n";
        }

        header = header.replace("{{class_full_name}}", clazz.getName());
        header = header.replace("{{id_field}}", idFiledName);

        String template = header + "insert into {{table}}\n" +
                "       <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >\n" +
                "           {{columns}}\n" +
                "       </trim>\n" +
                "       <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >\n" +
                "            {{values}}\n" +
                "        </trim>\n" +
                "</insert>"
                ;


        template = template.replace("{{table}}", getTableName(clazz));
        template = template.replace("{{columns}}", culumnXml);
        template = template.replace("{{values}}", valueXmls);
        return template;
    }

    public static String generateDeleteSql(Class<?> clazz){
        IdColumnInfo columnInfo = getIdColumn(clazz);
        String template = "<delete id=\"deleteByPrimaryKey\" parameterType=\"{{id_field_type}}\" >\n" +
                "                delete from {{table_name}} where {{id_column}} = #{ {{id_field}},jdbcType={{id_column_type}} }\n" +
                "    </delete>\n";
        template = template.replace("{{id_field_type}}", columnInfo.fieldType);
        template = template.replace("{{table_name}}", getTableName(clazz));
        template = template.replace("{{id_column}}", columnInfo.columnName);
        template = template.replace("{{id_field}}", columnInfo.fieldName);
        template = template.replace("{{id_column_type}}", columnInfo.columnType);

        return template;
    }

    public static String generateSelectById(Class<?> clazz){
        IdColumnInfo columnInfo = getIdColumn(clazz);
        String template = " <select id=\"selectByPrimaryKey\" resultMap=\"{{class_name}}\" parameterType=\"{{id_field_type}}\" >\n" +
                "        select\n" +
                "        <include refid=\"Base_Column_List\" />\n" +
                "        from {{table_name}}\n" +
                "        where {{id_column}} = #{ {{id_field}},jdbcType={{id_column_type}} }\n" +
                "    </select>\n";
        template = template.replace("{{class_name}}", clazz.getSimpleName());
        template = template.replace("{{id_field_type}}", columnInfo.fieldType);
        template = template.replace("{{table_name}}", getTableName(clazz));
        template = template.replace("{{id_column}}", columnInfo.columnName);
        template = template.replace("{{id_field}}", columnInfo.fieldName);
        template = template.replace("{{id_column_type}}", columnInfo.columnType);

        return template;
    }


    public static String getUpdateItem(ColumnInfo columnInfo, boolean ignoreNull){
        String template =  " <if test=\"{{field_name}} != null\" >\n" +
                "                {{column_name}} = #{ {{field_name}}, jdbcType={{column_type}} },\n" +
                "            </if>\n";
        if(!ignoreNull){
            template = "{{column_name}} = #{ {{field_name}}, jdbcType={{column_type}} },\n";
        }
        template = template.replace("{{field_name}}", columnInfo.fieldName);
        template = template.replace("{{field_name}}", columnInfo.fieldName);
        template = template.replace("{{column_name}}", columnInfo.columnName);
        template = template.replace("{{column_type}}", columnInfo.columnType);
        return template;
    }

    public static String generateUpdateById(Class<?> clazz, boolean ignoreNull){
        String template = "<update id=\"updateByPrimaryKeySelective\" parameterType=\"{{class_full_name}}\" >\n" +
                "        update {{table_name}}\n" +
                "        <set >\n" +
                "            {{items}}" +

                "        </set>\n" +
                "        where {{id_column}} = #{ {{id_field}},jdbcType={{id_column_type}} }\n" +
                "    </update>\n";

        List<ColumnInfo> columns = getColumnList(clazz);

        String valueXmls = "";
        IdColumnInfo idColumnInfo = null;
        for (ColumnInfo column: columns){
            if(column instanceof IdColumnInfo){
                if(((IdColumnInfo) column).autoIncrement){
                    idColumnInfo = (IdColumnInfo) column;
                    continue;
                }
            }
            valueXmls += getUpdateItem(column, ignoreNull) + "\n";
        }

        template = template.replace("{{class_full_name}}", clazz.getName());
        template = template.replace("{{items}}", valueXmls);
        template = template.replace("{{id_column}}", idColumnInfo.columnName);
        template = template.replace("{{id_field}}", idColumnInfo.fieldName);
        template = template.replace("{{id_column_type}}", idColumnInfo.columnType);
        template = template.replace("{{table_name}}", getTableName(clazz));

        return template;
    }



    public static void test(String[] args){
        System.out.println(generateBatisXmlHeader(SysAcl.class));
        System.out.println(generateInsertSql(SysAcl.class, false));
        System.out.println(generateInsertSql(SysAcl.class, true));
        System.out.println(generateDeleteSql(SysAcl.class));
        System.out.println(generateSelectById(SysAcl.class));
        System.out.println(generateUpdateById(SysAcl.class, false));
        System.out.println(generateUpdateById(SysAcl.class, true));
    }
}

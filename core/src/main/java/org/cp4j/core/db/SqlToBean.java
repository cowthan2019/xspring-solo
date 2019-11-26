package org.cp4j.core.db;

import org.cp4j.core.Lang;
import org.cp4j.core.utils.NameUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SqlToBean {


    public static String sqlToBean(String modelClass, String sql, Set<String> exclude) {
        if(exclude == null) exclude = new HashSet<>();
        // 取第一个(，最后一个)之间的内容
        // ,分隔开
        // 对于每一行，去掉`
        // 空格分隔，取前两个，第一个是字段名，第二个是类型int, tinyint, decimal, varchar, char, timestamp

        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("int", "Integer");
        typeMap.put("tinyint", "Integer");
        typeMap.put("bigint", "Integer");
        typeMap.put("decimal", "Double");
        typeMap.put("text", "String");
        typeMap.put("varchar", "String");
        typeMap.put("char", "String");
        typeMap.put("timestamp", "Date");

        int leftKuohaoIndex = sql.indexOf('(');
        int rightKuohaoIndex = sql.lastIndexOf(')');
        sql = sql.substring(leftKuohaoIndex + 1, rightKuohaoIndex);

        String[] colums = sql.split(",\n");

        String fields = "";
        for (int i = 0; i < Lang.count(colums); i++) {
            String colum = colums[i].trim().replace("`", "");
//            System.out.println(colum);
            String[] parts = colum.split(" ");
            String fieldName = NameUtils.lineToHump(parts[0]);
            if(exclude.contains(fieldName) || exclude.contains(parts[0])) continue;
            Set<String> ignore = new HashSet<>();
            ignore.add("PRIMARY");
            ignore.add("UNIQUE");
            ignore.add("INDEX");
            if(ignore.contains(fieldName.toUpperCase())) break;

            String dbType = parts[1];
            boolean maybeLong = false;
            if(dbType.contains("int(10)")) maybeLong = true;
            if(dbType.contains("int(11)")) maybeLong = true;
            if(dbType.contains("int(12)")) maybeLong = true;
            if(dbType.contains("(")){
                dbType = dbType.substring(0, dbType.indexOf('('));
            }
            String type = typeMap.get(dbType.toLowerCase());
            if(type == null) type = "String";
            if(type == "Integer" && maybeLong) type = "Long";


            fields += "    private " + type + " " + fieldName + ";\n";

        }



        String template = "import lombok.Data;\n" +
                "import java.util.Date;\n" +
                "\n" +
                "@Data\n" +
                "public class {{Model}} {\n" +
                "{{fields}}\n" +
                "}";
        template = template.replace("{{Model}}", modelClass);
        template = template.replace("{{fields}}", fields);

        System.out.println(template);
        return template;
    }


    public static void test(String[] args) {
        String modelClass = "AccountClass";
        String sql = "CREATE TABLE `t_auth`  (\n" +
                "  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                "  `username` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',\n" +
                "  `password` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码（AES加密）',\n" +
                "  `sid` varchar(15) NOT NULL DEFAULT '' COMMENT '主播短id，一般是7位数',\n" +
                "  `fid` varchar(30) NOT NULL  DEFAULT '' COMMENT '主播长id，一般是4位数',\n" +
                "  `uid` varchar(200) NOT NULL COMMENT 'user id',\n" +
                "  `token` varchar(200) NOT NULL COMMENT 'user token',\n" +
                "  `role_id` int(10) UNSIGNED NOT NULL COMMENT '角色ID',\n" +
                "  `status` tinyint(3) UNSIGNED NOT NULL COMMENT '状态：1可用,2禁用',\n" +
                "  `user_type` tinyint(3) UNSIGNED NOT NULL COMMENT '账号类型：1 邮箱, 2 手机, 3 facebook',\n" +
                "  `last_login_device` varchar(200) NOT NULL DEFAULT '' COMMENT '最近登录的设备号',\n" +
                "  `last_login_platform` varchar(20) NOT NULL DEFAULT '' COMMENT '最近登录的平台，android或者ios',\n" +
                "  `last_login_time` timestamp(0) COMMENT '上次登录时间',\n" +
                "  `gmt_create` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',\n" +
                "  `gmt_modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',\n" +
                "  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',\n" +
                "  PRIMARY KEY (`id`) USING BTREE,\n" +
                "  UNIQUE INDEX `unq_username`(`username`) USING BTREE,\n" +
                "  INDEX `idx_username`(`username`) USING BTREE,\n" +
                "  INDEX `idx_sid`(`sid`) USING BTREE,\n" +
                "  INDEX `idx_fid`(`fid`) USING BTREE,\n" +
                "  INDEX `idx_role_id`(`role_id`) USING BTREE,\n" +
                "  INDEX `idx_status`(`status`) USING BTREE\n" +
                ") ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;\n";

        sqlToBean(modelClass, sql, null);

    }
}

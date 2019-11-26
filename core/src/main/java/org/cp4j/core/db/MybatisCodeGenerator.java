package org.cp4j.core.db;

import org.cp4j.core.File2;
import org.cp4j.core.Lang;

import java.util.Set;

public class MybatisCodeGenerator {

    public static void main(String[] args) {

        String rootDir = "/Users/cowthan/Desktop/ws/java2019/1on1/db-template/";

        String table = "t_run_log";
        String module = "RunLog"; // 首字母大写
        String desc = "运行日志";
        String router = module.toLowerCase();
        String sql = "CREATE TABLE `t_run_log`  (\n" +
                "  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                "  `main_id` varchar(200) NOT NULL DEFAULT '' COMMENT '全局id，表示一个动作，或一个请求，或一个任务，甚至是分布式的任务，对于已存在的main_id，应该做update操作',\n" +
                "  `platform` varchar(20) NOT NULL DEFAULT 'server' COMMENT '状态：android, ios, server',\n" +
                "  `log_type` varchar(20) NOT NULL DEFAULT '' COMMENT '各个平台自己定义的日志类型，如im回调日志 im_callback，im服务器api调用日志 im_api',\n" +
                "  `status` varchar(20) NOT NULL DEFAULT '' COMMENT '根据log_type自定义的任务阶段标志',\n" +
                "  `ip` varchar(50)  NOT NULL DEFAULT '' COMMENT '服务器ip',\n" +
                "  `title` varchar(200) NOT NULL  DEFAULT ''  COMMENT '主题',\n" +
                "  `body` text NOT NULL  DEFAULT '' COMMENT '日志信息-json',\n" +
                "  `deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '0正常 1删除',\n" +
                "  `gmt_create` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',\n" +
                "  `gmt_modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',\n" +
                "  PRIMARY KEY (`id`) USING BTREE,\n" +
                "  INDEX `index_log_type`(`log_type`) USING BTREE\n" +
                ") ";

        String outDir = rootDir + "out/";

        File2.deleteFileOrDir(outDir);

        // model
        String modelName = module + "Model";
        {
            String modelContent = SqlToBean.sqlToBean(modelName, sql, null);
            Lang.file_put_content(outDir + modelName + ".java", modelContent);

        }

        // form, vo
        String formName = module + "Form";
        String formQueryName = module + "QueryForm";
        String voName = module + "VO";
        {

            Set<String> exclude = Lang.newHashSet("deleted", "gmt_create", "gmt_modified");
            String formContent = SqlToBean.sqlToBean(formName, sql, exclude);
            Lang.file_put_content(outDir  + "app/" + formName + ".java", formContent);

            String voContent = SqlToBean.sqlToBean(voName, sql, exclude);
            Lang.file_put_content(outDir  + "app/" + voName + ".java", voContent);

            String templateQueryMorm = rootDir + "CommonQueryMorm.java";
            String content = Lang.file_get_content(templateQueryMorm);
            content = content.replace("{{Class}}", formQueryName);
            Lang.file_put_content(outDir  + "app/" + formQueryName + ".java", content);

        }

        // morm, mo queryMorm
        String mormName = module + "Morm";
        String mormQueryName = module + "QueryMorm";
        String moName = module + "MO";
        {
            Set<String> exclude = Lang.newHashSet("deleted", "gmt_create", "gmt_modified");
            String formContent = SqlToBean.sqlToBean(mormName, sql, exclude);
            Lang.file_put_content(outDir +"admin/" + mormName + ".java", formContent);

            String voContent = SqlToBean.sqlToBean(moName, sql, exclude);
            Lang.file_put_content(outDir +"admin/" + moName + ".java", voContent);

            String templateQueryMorm = rootDir + "CommonQueryMorm.java";
            String content = Lang.file_get_content(templateQueryMorm);
            content = content.replace("{{Class}}", mormQueryName);
            Lang.file_put_content(outDir +"admin/" + mormQueryName + ".java", content);


        }

        // dao, service
        String outDaoClass = module + "Dao";
        String outServiceClass = module + "Service";
        String outServiceImplClass = module + "ServiceImpl";

        String templateDao = rootDir + "CommonDao.java";
        String templateService = rootDir + "CommonService.java";
        String templateService2 = rootDir + "CommonService2.java";
        String templateServiceImpl = rootDir + "CommonServiceImpl.java";

        {
            String daoContent = Lang.file_get_content(templateDao);
            daoContent = daoContent.replace("{{Class}}", outDaoClass);
            daoContent = daoContent.replace("{{Model}}", modelName);
            daoContent = daoContent.replace("{{table}}", table);
            Lang.file_put_content(outDir + outDaoClass + ".java", daoContent);

            String serviceContent = Lang.file_get_content(templateService);
            serviceContent = serviceContent.replace("{{Class}}", outServiceClass);
            serviceContent = serviceContent.replace("{{Model}}", modelName);
            Lang.file_put_content(outDir + outServiceClass + ".java", serviceContent);

            String serviceImplContent = Lang.file_get_content(templateServiceImpl);
            serviceImplContent = serviceImplContent.replace("{{Class}}", outServiceImplClass);
            serviceImplContent = serviceImplContent.replace("{{Model}}", modelName);
            serviceImplContent = serviceImplContent.replace("{{Service}}", outServiceClass);
            serviceImplContent = serviceImplContent.replace("{{Dao}}", outDaoClass);
            Lang.file_put_content(outDir + outServiceImplClass + ".java", serviceImplContent);

            serviceImplContent = Lang.file_get_content(templateService2);
            serviceImplContent = serviceImplContent.replace("{{Class}}", outServiceClass + "2");
            serviceImplContent = serviceImplContent.replace("{{Model}}", modelName);
            serviceImplContent = serviceImplContent.replace("{{Dao}}", outDaoClass);
            Lang.file_put_content(outDir + outServiceClass + "2.java", serviceImplContent);
        }

        // api controller
        String templateController = rootDir + "CommonController.java";
        String outControllerClass = module + "Controller";
        {
            String controllerContent = Lang.file_get_content(templateController);
            controllerContent = controllerContent.replace("{{Class}}", outControllerClass);
            controllerContent = controllerContent.replace("{{FormClass}}", formName);
            controllerContent = controllerContent.replace("{{Model}}", modelName);
            controllerContent = controllerContent.replace("{{VO}}", voName);
            controllerContent = controllerContent.replace("{{Service}}", outServiceClass);
            controllerContent = controllerContent.replace("{{serviceFieldName}}", (outServiceClass.charAt(0)+"").toLowerCase() + outServiceClass.substring(1));
            controllerContent = controllerContent.replace("{{router}}", router);
            Lang.file_put_content(outDir  + "app/" + outControllerClass + ".java", controllerContent);
        }

        // admin controller
        String templateAdminController = rootDir + "AdminCommonController.java";
        String outAdminControllerClass = "Admin" + module + "Controller";
        {
            String  controllerContent = Lang.file_get_content(templateAdminController);
            controllerContent = controllerContent.replace("{{Class}}", outAdminControllerClass);
            controllerContent = controllerContent.replace("{{Service}}", outServiceClass);
            controllerContent = controllerContent.replace("{{MormClass}}", mormName);
            controllerContent = controllerContent.replace("{{QueryMormClass}}", mormQueryName);
            controllerContent = controllerContent.replace("{{MO}}", moName);
            controllerContent = controllerContent.replace("{{Model}}", modelName);
            controllerContent = controllerContent.replace("{{router}}", router);
            controllerContent = controllerContent.replace("{{desc}}", desc);
            controllerContent = controllerContent.replace("{{table}} ", table);
            Lang.file_put_content(outDir + "admin/" + outAdminControllerClass + ".java", controllerContent);

        }

        // vue
        {
            String tplVueApi = rootDir + "vue/api.js";
            String tplVueIndex = rootDir + "vue/index.vue";
            String tplVueCreate = rootDir + "vue/create.vue";

            String content = Lang.file_get_content(tplVueApi);
            content = content.replace("{{router}}", router);
            Lang.file_put_content(outDir  + "vue/" + router +".js", content);


            content = Lang.file_get_content(tplVueIndex);
            content = content.replace("{{router}}", router);
            content = content.replace("{{desc}}", desc);
            Lang.file_put_content(outDir  + "vue/" + "index" +".vue", content);


            content = Lang.file_get_content(tplVueCreate);
            content = content.replace("{{router}}", router);
            content = content.replace("{{desc}}", desc);
            Lang.file_put_content(outDir  + "vue/" + "create" +".vue", content);
        }


        System.out.println("生成完毕，输出目录：" + outDir);
    }

}

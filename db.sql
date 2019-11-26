DROP TABLE IF EXISTS `t_auth`;
CREATE TABLE `t_auth`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(200)  NOT NULL  DEFAULT '' COMMENT '用户名',
  `password` varchar(400)  NOT NULL  DEFAULT '' COMMENT '密码',
  `uid` varchar(100) NOT NULL COMMENT 'user id',
  `sid` varchar(15) NOT NULL DEFAULT '' COMMENT '主播短id，一般是4位数，根据用户数量加长',
  `token` varchar(100) NOT NULL COMMENT 'user token',
  `role_id` int(10) UNSIGNED NOT NULL COMMENT '角色ID',
  `vip_level` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'vip等级，0是普通用户',
  `status` tinyint(3) UNSIGNED NOT NULL COMMENT '状态：0可用, 1禁用',
  `user_type` tinyint(3) UNSIGNED NOT NULL COMMENT '账号类型：1 邮箱, 2 手机, 3 三方平台',
  `last_login_device` varchar(200) NOT NULL DEFAULT '' COMMENT '最近登录的设备号',
  `last_login_platform` varchar(20) NOT NULL DEFAULT '' COMMENT '最近登录的平台，android或者ios',
  `last_login_time` timestamp(0) COMMENT '上次登录时间',
  `deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `gmt_create` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `gmt_modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unq_username`(`username`) USING BTREE,
  UNIQUE INDEX `idx_uid`(`uid`) USING BTREE,
  UNIQUE INDEX `idx_token`(`token`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

INSERT INTO `t_auth` (`username`, `password`, `sid`, `uid`, `token`, `role_id`, `vip_level`, `status`, `user_type`)
VALUES
	('admin', 'a016ccfb45e6203830224d9a38521e08', '9527', '9527', '9527', 1, 0, 0, 2);


DROP TABLE IF EXISTS `t_auth2_binding`;
CREATE TABLE `t_auth2_binding`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(10) NOT NULL DEFAULT 0 COMMENT '用户id',
  `platform` varchar(30) NOT NULL DEFAULT '' COMMENT '平台，facebook, wx, qq, weibo, google等',
  `open_id` varchar(100) NOT NULL DEFAULT '' COMMENT '三方平台的user id',
  `name` varchar(100) NOT NULL  DEFAULT '' COMMENT '三方平台的昵称，名字等',
  `head_icon` varchar(500) NOT NULL  DEFAULT '' COMMENT '三方平台的头像',
  `gender` tinyint(3) NOT NULL DEFAULT 0 COMMENT '性别，0表示未知，1表示男，2女表示女',
  `status` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态：0可用, 1禁用，解绑了之类的',
  `deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `gmt_create` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `gmt_modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_open_id`(`open_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户三方平台账号绑定表--oauth2' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `t_verify_code`;
CREATE TABLE `t_verify_code`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号或邮箱',
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '验证码',
  `start_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始计时时间',
  `deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `gmt_create` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `gmt_modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '验证码表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `t_user_profile`;
CREATE TABLE `t_user_profile` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(10) NOT NULL COMMENT '用户id',
  `nickname` varchar(64) NOT NULL DEFAULT '' COMMENT '昵称',
  `head_icon` varchar(1024) NOT NULL DEFAULT '' COMMENT '姓名',
  `signature` varchar(256) NOT NULL DEFAULT '' COMMENT '签名',
  `realname` varchar(24) NOT NULL DEFAULT '' COMMENT '姓名',
  `gender` tinyint(3) NOT NULL DEFAULT 0 COMMENT '性别，0表示未知，1表示男，2女表示女',
  `email` varchar(100) NOT NULL DEFAULT '' COMMENT '邮箱',
  `age` int(9) NOT NULL DEFAULT 0 COMMENT '年龄',
  `birth` varchar(24) NOT NULL DEFAULT '' COMMENT '生日',
  `mobile` varchar(32) NOT NULL DEFAULT '' COMMENT '用户mobile，最大长度32字符，非中国大陆手机号码需要填写国家代码(如美国：+1-xxxxxxxxxx)或地区代码(如香港：+852-xxxxxxxx)，可设置为空字符串',
  `address` varchar(200) NOT NULL DEFAULT '' COMMENT '地址',
  `extra` varchar(1024) NOT NULL DEFAULT '' COMMENT '扩展字段',
  `deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `gmt_create` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `gmt_modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户资料表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `t_error_log`;
CREATE TABLE `t_error_log`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `main_id` varchar(200) NOT NULL DEFAULT '' COMMENT '全局id，表示一个动作，或一个请求，或一个任务，甚至是分布式的任务，对于已存在的main_id，应该做update操作',
  `platform` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态：1 android, 2 ios 3 server',
  `log_type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态：1 业务log, 2 waning日志，3 可接受异常log， 4 崩溃异常，其他：各个平台自己定义的日志类型',
   `status` tinyint(3) UNSIGNED NOT NULL DEFAULT 1 COMMENT '0 未处理 1 进行中 2 处理完毕',
  `ip` varchar(50)  NOT NULL DEFAULT '' COMMENT '服务器ip',
  `md5` varchar(200) NOT NULL  DEFAULT ''  COMMENT '异常内容的md5',
  `title` varchar(200) NOT NULL  DEFAULT ''  COMMENT '主题',
  `extra` text NOT NULL  DEFAULT '' COMMENT '附加信息',
  `times` int(10) NOT NULL DEFAULT 1 COMMENT '出现次数',
  `first_show_time` int(10) NOT NULL DEFAULT 0 COMMENT '首次出现时间',
  `last_show_time` int(10) NOT NULL DEFAULT 0 COMMENT '最后出现时间',
  `deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '0正常 1删除',
  `gmt_create` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `gmt_modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_log_type`(`log_type`) USING BTREE,
  INDEX `index_last_show_time`(`last_show_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日志表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `t_run_log`;
CREATE TABLE `t_run_log`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `main_id` varchar(200) NOT NULL DEFAULT '' COMMENT '全局id，表示一个动作，或一个请求，或一个任务，甚至是分布式的任务，对于已存在的main_id，应该做update操作',
  `platform` varchar(20) NOT NULL DEFAULT 'server' COMMENT '状态：android, ios, server',
  `log_type` varchar(20) NOT NULL DEFAULT '' COMMENT '各个平台自己定义的日志类型，如im回调日志 im_callback，im服务器api调用日志 im_api',
  `status` varchar(20) NOT NULL DEFAULT '' COMMENT '根据log_type自定义的任务阶段标志',
  `ip` varchar(50)  NOT NULL DEFAULT '' COMMENT '服务器ip',
  `title` varchar(200) NOT NULL  DEFAULT ''  COMMENT '主题',
  `body` text NOT NULL  DEFAULT '' COMMENT '日志信息-json',
  `deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '0正常 1删除',
  `gmt_create` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `gmt_modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_log_type`(`log_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '运行日志表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `t_op_log`;
CREATE TABLE `t_op_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(10) NOT NULL DEFAULT '0' COMMENT '操作者',
  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '操作者',
  `title` varchar(300) NOT NULL DEFAULT '' COMMENT '日志标题',
  `content` varchar(300) NOT NULL DEFAULT '' COMMENT '日志描述',
  `ip` varchar(100) NOT NULL DEFAULT '' COMMENT 'ip地址',
  `location` varchar(100) NOT NULL DEFAULT '' COMMENT 'IP地址对应的地点',
  `module` varchar(100) NOT NULL DEFAULT '' COMMENT '修改的模块',
  `module_id` int(10) NOT NULL DEFAULT '0' COMMENT '模块对应的主表id',
  `action` varchar(10) NOT NULL DEFAULT '' COMMENT 'insert, update, delete',
  `op_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `request_type` varchar(10) NOT NULL DEFAULT '' COMMENT '请求方法',
  `request_url` varchar(50) NOT NULL DEFAULT '' COMMENT '请求地址',
  `request_param` varchar(2000) NOT NULL DEFAULT '' COMMENT '请求参数',
  `cost` int(9) NOT NULL DEFAULT '0' COMMENT '请求花费时间',
  `deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='后台操作日志表';


DROP TABLE IF EXISTS `t_access_log`;
CREATE TABLE `t_access_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(10) NOT NULL DEFAULT '0' COMMENT '操作者',
  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '操作者',
  `title` varchar(300) NOT NULL DEFAULT '' COMMENT '日志标题',
  `content` varchar(300) NOT NULL DEFAULT '' COMMENT '日志描述',
  `ip` varchar(100) NOT NULL DEFAULT '' COMMENT 'ip地址',
  `location` varchar(100) NOT NULL DEFAULT '' COMMENT 'IP地址对应的地点',
  `module` varchar(100) NOT NULL DEFAULT '' COMMENT '修改的模块',
  `module_id` int(10) NOT NULL DEFAULT '0' COMMENT '模块对应的主表id',
  `action` varchar(10) NOT NULL DEFAULT '' COMMENT 'insert, update, delete',
  `op_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `request_type` varchar(10) NOT NULL DEFAULT '' COMMENT '请求方法',
  `request_url` varchar(50) NOT NULL DEFAULT '' COMMENT '请求地址',
  `request_param` varchar(2000) NOT NULL DEFAULT '' COMMENT '请求参数',
  `cost` int(9) NOT NULL DEFAULT '0' COMMENT '请求花费时间',
  `deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='访问日志表';




DROP TABLE IF EXISTS `t_banner`;
CREATE TABLE `t_banner` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `location` varchar(20) NOT NULL DEFAULT 0 COMMENT '轮播图所属页面, none不是任何页面 home主页',
  `type` int(10) NOT NULL DEFAULT 1 COMMENT '1 url 2 其他暂不处理',
  `title` varchar(40) NOT NULL DEFAULT '' COMMENT '标题',
  `summary` varchar(100) NOT NULL DEFAULT '' COMMENT '描述',
  `media_url` varchar(1024) NOT NULL DEFAULT '' COMMENT '图片或视频地址',
  `media_thumb` varchar(200) NOT NULL DEFAULT '' COMMENT '视频的缩略图',
  `media_type` varchar(20) NOT NULL DEFAULT 1 COMMENT 'image, video, audio等',
  `redirect_url` varchar(1024) NOT NULL DEFAULT '' COMMENT '跳转地址',
  `sort` int(10) NOT NULL DEFAULT 0 COMMENT '排序key',
  `status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '0正常 1下架',
  `deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '0正常 1删除',
  `gmt_create` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `gmt_modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '轮播图' ROW_FORMAT = Dynamic;

INSERT INTO `t_banner` (`location`, `type`, `title`, `summary`, `media_url`, `media_thumb`, `media_type`, `redirect_url`, `status`, `sort`)
VALUES
	('home', 1, 'title', 'summary', 'http://pingus.oss-cn-beijing.aliyuncs.com/cdn/img/11.jpg', '', 1, 'https://www.baidu.com/', 0, 1),
	('home', 1, 'title', 'summary', 'http://pingus.oss-cn-beijing.aliyuncs.com/cdn/img/timg%20(10).jpeg', '', 1, 'https://www.baidu.com/', 0, 2),
	('home', 1, 'title', 'summary', 'http://pingus.oss-cn-beijing.aliyuncs.com/cdn/img/timg%20(12).jpeg', '', 1, 'https://www.baidu.com/', 0, 3),
	('home', 1, '轮播4', '轮播4的summary', 'http://pingus.oss-cn-beijing.aliyuncs.com/cdn/img/timg%20(6).jpeg', '', 1, 'http://www.baidu.com', 0, 1);


DROP TABLE IF EXISTS `t_device`;
CREATE TABLE `t_device`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `platform` varchar(10) NOT NULL DEFAULT  '' COMMENT 'android, ios',
  `device_id` varchar(100)  NOT NULL DEFAULT '' COMMENT '设备号',
  `brand` varchar(100) NOT NULL DEFAULT '' COMMENT '手机型号',
  `sdk` varchar(100) NOT NULL DEFAULT '' COMMENT 'sdk版本',
  `extra` varchar(2000) NOT NULL DEFAULT '' COMMENT '附加信息',
  `app_version` int(9) NOT NULL DEFAULT 0 COMMENT 'app版本',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `gmt_create` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `gmt_modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unq_device_id`(`device_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备统计表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `t_app_version`;
CREATE TABLE `t_app_version` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `platform` varchar(10) DEFAULT 0 COMMENT 'android, ios',
  `version_code` int(10) NOT NULL COMMENT '版本code',
  `version_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '版本号',
  `change_log` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新日志，斜杠n分隔',
  `force` tinyint(3) DEFAULT 0 COMMENT '1 强制更新 0 非强制更新',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '下载地址',
    `status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '0正常 2下架',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `gmt_create` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `gmt_modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'app版本表' ROW_FORMAT = Dynamic;

INSERT INTO `t_app_version` (`id`, `platform`, `version_code`, `version_name`, `change_log`, `force`, `url`, `status`, `deleted`, `gmt_create`, `gmt_modified`)
VALUES
	(1, 'android', 1, 'v1.0.0', '么啥，改了个bug，引入了三个其他bug', 0, 'http://pingus.oss-cn-beijing.aliyuncs.com/cdn/app-debug.apk', 0, 0, '2019-11-25 20:27:27', '2019-11-25 20:27:53');

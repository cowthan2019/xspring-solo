package com.danger;

import com.danger.common.dao.SchemeDao;
import com.danger.config.Env;
import com.danger.job.QuartzManager;
import com.danger.job.task.NoticeJob;
import org.cp4j.core.*;
import org.cp4j.core.utils.LinuxUsageUtils;
import org.cp4j.core.utils.Logs;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicLong;

//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration .class})
@EnableAutoConfiguration()
@SpringBootApplication
@RestController
@ComponentScan(basePackages = {"com.danger"})
@MapperScan("com.danger.repository")
@ServletComponentScan("com.danger")
@Configuration
public class App implements CommandLineRunner, ApplicationContextAware {

    public static AtomicLong REQUEST_COUNT = new AtomicLong();
    public static final String VERSION = "V1.0.0";
    public static final String CHANGE_LOG = "";
    public static final long START_TIME = System.currentTimeMillis();

    public static void main(String[] args) {
        App.context = SpringApplication.run(App.class, args);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //注入的是AnnotationConfigServletWebServerApplicationContext
        Logs.info("全局ApplicationContext注入：" + applicationContext.getClass().getSimpleName());
        App.context = applicationContext;
    }

    @Override
    public void run(String... strings) throws Exception {
        app = this;
        String startTime = Lang.toDate("yyyy-MM-dd HH:mm:ss", App.START_TIME / 1000);
        String startTime2 = Lang.toDate("yyyy-MM-dd-HH:mm:ss", App.START_TIME / 1000);
        Logs.info("-----------加载全局配置-----------");
        Logs.info(JsonUtils.toJsonPretty(env()));
        Env.init();

        Logs.info("-----------加载邮件配置：阿里DirectMail-----------");
        App.env().initMailForAli();
//        boolean mailResult = NotifyUtils.sendEmail(Lang.newArrayList(""), "应用启动", "启动时间：" + startTime);

        Logs.info("-------------开启定时任务-------------");
        QuartzManager.addJob("job_name", "job_group",
                "trigger", "trigger_group",
                NoticeJob.class, "*/30 * * * * ?");
        Logs.info("--------------------------------");


//		TestMain.main(context);
    }

    public static App app;

    public static ApplicationContext context;

    @Autowired
    private Env env;

    public static Env env() {
        return App.app.env;
    }

    @Autowired
    SchemeDao schemeDao;

    @RequestMapping(value = "check/health")
    public MyResponse getServerInfo(HttpServletRequest request) {
        AssocArray array = AssocArray.array();
        String error = "";
        array.add("version", App.VERSION);
        array.add("change_log", App.CHANGE_LOG);
        array.add("start_at", Lang.toDate("yyyy-MM-dd HH:mm:ss", App.START_TIME / 1000));
        array.add("mac", Kits.getMac());

        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
            array.add("ip", addr.getHostAddress());
            String hostName = addr.getHostName(); //获取本机计算机名称
            array.add("host", hostName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        array.add("charset", Charset.defaultCharset());
        array.add("request_count", App.REQUEST_COUNT.get());

        // check 机器负载
        try {
            array.add("usage_cpu", LinuxUsageUtils.getUsageCPU());
            array.add("usage_memory", LinuxUsageUtils.getUsageMemory());
            array.add("usage_disk", LinuxUsageUtils.getUsageDisk());
            array.add("usage_network", LinuxUsageUtils.getUsageNetwork());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // check 数据库
        try {
            schemeDao.select("desc t_auth", AssocArray.array());
            array.add("db_ok", true);
        } catch (Exception e) {
            error = e.getMessage();
            array.add("db_ok", false);
        }

        array.add("error", error);
        return MyResponse.ok(array);
    }
}

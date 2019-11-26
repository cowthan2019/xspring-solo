package com.danger.job.task;

import com.danger.App;
import org.cp4j.core.Lang;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoticeJob implements Job {

    private static Logger logger = LoggerFactory.getLogger(NoticeJob.class);
    private static int a = 0;



    public void execute(JobExecutionContext context) throws JobExecutionException {
        a++;
        System.out.println("开始时间---" + Lang.toDate("yyyy-MM-dd HH:mm:ss", App.START_TIME/1000));


    }

}

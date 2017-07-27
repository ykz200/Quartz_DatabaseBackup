package com.bhusk.quartz.Job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * kzyuan Job 参考
 * @description black husk
 * @description http://www.bhusk.com
 */
public class MyJob extends QuartzJobBean {

    private static Logger logger = LoggerFactory.getLogger(DatabaseBackupJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        System.out.println("执行时间:"+new Date());
    }
}

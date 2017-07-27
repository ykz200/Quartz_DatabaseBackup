package com.bhusk.quartz.Job;

import com.bhusk.quartz.Util.EmailManager;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * 黑壳网数据库定时备份
 * @description black husk
 @description http://www.bhusk.com
 */
public class DatabaseBackupJob extends QuartzJobBean {
    private static Logger logger = LoggerFactory.getLogger(DatabaseBackupJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        String from = "yuankz888@163.com";
        String[] to = {"lu12121@qq.com", "keshu@bhusk.com"};
        String[] copyto = {""};
        String subject = "黑壳网数据库备份";
        String content = "没有数据就没有一切，数据库备份就是一种防范灾难于未然的强力手段，没有了数据，应用再花哨也是镜中花水中月。";
        String[] fileList = new String[2];
        fileList[0] = "/Users/kzyuan/solo_h2/db.mv.db";
        fileList[1] = "/Users/kzyuan/solo_h2/db.trace.db";
        EmailManager.getInstance().sendMail(from, to, copyto, subject, content, fileList);
        logger.info("黑壳网数据库备份时间:"+new Date());
        System.out.println("执行时间:"+new Date());

    }
}

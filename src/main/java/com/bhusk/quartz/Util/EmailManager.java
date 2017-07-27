package com.bhusk.quartz.Util;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 邮件管理器
 * java 实现邮件的发送， 抄送及多附件
 */
public class EmailManager {

    public static String username = "keshu@bhusk.com"; // 服务邮箱(from邮箱)
    public static String password = ""; // 邮箱密码
    public static String senderNick = "黑壳网的壳叔";   // 发件人昵称

    private Properties props; // 系统属性
    private Session session; // 邮件会话对象
    private MimeMessage mimeMsg; // MIME邮件对象
    private Multipart mp;   // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象

    private static EmailManager instance = null;

    public EmailManager() {
        props = System.getProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.163.com");
        props.put("mail.smtp.port", "25");
        props.put("username", username);
        props.put("password", password);
        // 建立会话
        session = Session.getDefaultInstance(props);
        session.setDebug(false);
    }

    public static EmailManager getInstance() {
        if (instance == null) {
            instance = new EmailManager();
        }
        return instance;
    }

    /**
     * 发送邮件
     *
     * @param from     发件人
     * @param to       收件人
     * @param copyto   抄送
     * @param subject  主题
     * @param content  内容
     * @param fileList 附件列表
     * @return
     */
    public boolean sendMail(String from, String[] to, String[] copyto, String subject, String content, String[] fileList) {
        boolean success = true;
        try {
            mimeMsg = new MimeMessage(session);
            mp = new MimeMultipart();

            // 自定义发件人昵称
            String nick = "";
            try {
                nick = javax.mail.internet.MimeUtility.encodeText(senderNick);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 设置发件人
//          mimeMsg.setFrom(new InternetAddress(from));
            mimeMsg.setFrom(new InternetAddress(from, nick));
            // 设置收件人
            if (to != null && to.length > 0) {
                String toListStr = getMailList(to);
                mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toListStr));
            }
            // 设置抄送人
            if (copyto != null && copyto.length > 0) {
                String ccListStr = getMailList(copyto);
                mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccListStr));
            }
            // 设置主题
            mimeMsg.setSubject(subject);
            // 设置正文
            BodyPart bp = new MimeBodyPart();
            bp.setContent(content, "text/html;charset=utf-8");
            mp.addBodyPart(bp);
            // 设置附件
            if (fileList != null && fileList.length > 0) {
                for (int i = 0; i < fileList.length; i++) {
                    bp = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(fileList[i]);
                    bp.setDataHandler(new DataHandler(fds));
                    bp.setFileName(MimeUtility.encodeText(fds.getName(), "UTF-8", "B"));
                    mp.addBodyPart(bp);
                }
            }
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
            // 发送邮件
            if (props.get("mail.smtp.auth").equals("true")) {
                Transport transport = session.getTransport("smtp");
                transport.connect((String) props.get("mail.smtp.host"), (String) props.get("username"), (String) props.get("password"));
//              transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
//              transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.CC));
                transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
                transport.close();
            } else {
                Transport.send(mimeMsg);
            }
            System.out.println("邮件发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            success = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    /**
     * 发送邮件
     *
     * @param from     发件人
     * @param to       收件人, 多个Email以英文逗号分隔
     * @param cc       抄送, 多个Email以英文逗号分隔
     * @param subject  主题
     * @param content  内容
     * @param fileList 附件列表
     * @return
     */
    public boolean sendMail(String from, String to, String cc, String subject, String content, String[] fileList) {
        boolean success = true;
        try {
            mimeMsg = new MimeMessage(session);
            mp = new MimeMultipart();

            // 自定义发件人昵称
            String nick = "";
            try {
                nick = javax.mail.internet.MimeUtility.encodeText(senderNick);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 设置发件人
//          mimeMsg.setFrom(new InternetAddress(from));
            mimeMsg.setFrom(new InternetAddress(from, nick));
            // 设置收件人
            if (to != null && to.length() > 0) {
                mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            }
            // 设置抄送人
            if (cc != null && cc.length() > 0) {
                mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            }
            // 设置主题
            mimeMsg.setSubject(subject);
            // 设置正文
            BodyPart bp = new MimeBodyPart();
            bp.setContent(content, "text/html;charset=utf-8");
            mp.addBodyPart(bp);
            // 设置附件
            if (fileList != null && fileList.length > 0) {
                for (int i = 0; i < fileList.length; i++) {
                    bp = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(fileList[i]);
                    bp.setDataHandler(new DataHandler(fds));
                    bp.setFileName(MimeUtility.encodeText(fds.getName(), "UTF-8", "B"));
                    mp.addBodyPart(bp);
                }
            }
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
            // 发送邮件
            if (props.get("mail.smtp.auth").equals("true")) {
                Transport transport = session.getTransport("smtp");
                transport.connect((String) props.get("mail.smtp.host"), (String) props.get("username"), (String) props.get("password"));
                transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
                transport.close();
            } else {
                Transport.send(mimeMsg);
            }
            System.out.println("邮件发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            success = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public String getMailList(String[] mailArray) {
        StringBuffer toList = new StringBuffer();
        int length = mailArray.length;
        if (mailArray != null && length < 2) {
            toList.append(mailArray[0]);
        } else {
            for (int i = 0; i < length; i++) {
                toList.append(mailArray[i]);
                if (i != (length - 1)) {
                    toList.append(",");
                }

            }
        }
        return toList.toString();
    }

    public static void main(String[] args) {
        String from = username;
        String[] to = {"keshu@bhusk.com", "1520812121@qq.com"};
        String[] copyto = {"lu12121@qq.com"};
        String subject = "黑壳网数据库备份";
        String content = "没有数据就没有一切，数据库备份就是一种防范灾难于未然的强力手段，没有了数据，应用再花哨也是镜中花水中月。";
        String[] fileList = new String[2];
        fileList[0] = "~/solo_h2/db.mv.db";
        fileList[1] = "~/solo_h2/db.trace.db";

        EmailManager.getInstance().sendMail(from, to, copyto, subject, content, fileList);
    }
}
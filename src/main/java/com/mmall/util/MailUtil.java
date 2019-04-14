package com.mmall.util;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

/**
 * 邮件工具类
 * http://blog.csdn.net/u012661010/article/details/71946312
 */
public class MailUtil {
    public static boolean sendMail(String subject, String mailContent, String receiveAddress) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.163.com");//指定邮件的发送服务器地址
        props.put("mail.smtp.auth", "true");//服务器是否要验证用户的身份信息

        Session session = Session.getInstance(props);//得到Session
        session.setDebug(true);//代表启用debug模式，可以在控制台输出SMTP协议应答的过程


        //创建一个MimeMessage格式的邮件
        MimeMessage message = new MimeMessage(session);

        try {
            //设置发送者
            Address fromAddress = new InternetAddress("scushare@163.com");//邮件地址
            message.setFrom(fromAddress);//设置发送的邮件地址
            //设置接收者
            Address toAddress = new InternetAddress(receiveAddress);//要接收邮件的邮箱
            message.setRecipient(RecipientType.TO, toAddress);//设置接收者的地址

            //设置邮件的主题
            message.setSubject(subject);
            //设置邮件的内容
            message.setContent(mailContent, "text/html; charset=utf-8");
            //message.setText(mailContent);
            //保存邮件
            message.saveChanges();


            //得到发送邮件的服务器(这里用的是SMTP服务器)
            Transport transport = session.getTransport("smtp");

            //发送者的账号连接到SMTP服务器上  @163.com可以不写
            transport.connect("smtp.163.com", "scushare@163.com", "VonnyedBedTor2IX");
            //发送信息
            transport.sendMessage(message, message.getAllRecipients());
            //关闭服务器通道
            transport.close();
        } catch (Exception exception) {
            return false;
        }
        return true;
    }
}

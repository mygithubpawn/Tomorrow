package com.pawn.blog.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/***
 * description: 异步邮件发送实现类
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/20 20:50
 */

@Service
public class TaskService {
    @Autowired
    JavaMailSenderImpl mailSender;
    @Value("${spring.mail.username}")
    String userName;

    @Async
    public void sendEmailVerifyCode(String verifyCode, String emailAddress) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("博客注册验证码");
        message.setFrom("Tomorrow");
        message.setText("您的验证码为：" + verifyCode + "有效期为10分钟，若非本人操作，请忽略此邮件！");
        //发件人
        message.setFrom(userName);
        //收件人
        message.setTo(emailAddress);
        mailSender.send(message);
    }

    @Async
    public void emailArticle(String emailAddress, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("邮箱修改");
        message.setFrom("Tomorrow");
        message.setText(name + "道友，您的邮箱：已被修改，若非本人操作，请联系管理员，交流群：722633922");
        //发件人
        message.setFrom(userName);
        //收件人
        message.setTo(emailAddress);
        mailSender.send(message);
    }

    @Async
    public void emailArticleVia(String emailAddress, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("密码修改");
        message.setFrom("Tomorrow");
        message.setText(name + "道友，您的密码：已被修改，若非本人操作，请联系管理员，交流群：722633922");
        //发件人
        message.setFrom(userName);
        //收件人
        message.setTo(emailAddress);
        mailSender.send(message);
    }

    @Async
    public void feedbackContent(String SenderEmail, String name, String feedbackContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("用户反馈");
        message.setFrom("Tomorrow");
        message.setText("道友" + name + "提交了用户反馈，反馈内容：" + feedbackContent);
        //发件人
        message.setFrom(SenderEmail);
        //收件人
        message.setTo(userName);
        mailSender.send(message);
    }
@Async
    public void leaveMessageContent(String SenderEmail, String name, String feedbackContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("用户留言");
        message.setFrom("Tomorrow");
        message.setText("道友" + name + "提交了留言，留言内容：" + feedbackContent);
        //发件人
        message.setFrom(SenderEmail);
        //收件人
        message.setTo(userName);
        mailSender.send(message);
    }

    @Async
    public void userOperating(String SenderEmail, String name, String feedbackContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("管理员提醒");
        message.setFrom("Tomorrow");
        message.setText(name + "道友" + feedbackContent);
        //发件人
        message.setFrom(userName);
        //收件人
        message.setTo(SenderEmail);
        mailSender.send(message);
    }

}

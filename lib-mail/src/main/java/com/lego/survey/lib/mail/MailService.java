package com.lego.survey.lib.mail;

import com.lego.survey.lib.mail.model.MailBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
@Component
public class MailService {

    @Value("${define.email.sender}")
    private String sender;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 发送 文本邮件
     * @param mailBean
     */
    public void sendTxtMail(MailBean mailBean) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        //邮件发送人
        simpleMailMessage.setFrom(sender);
        //邮件接收人
        simpleMailMessage.setTo(mailBean.getRecipient());
        //邮件主题
        simpleMailMessage.setSubject(mailBean.getSubject());
        //邮件内容
        simpleMailMessage.setText(mailBean.getContent());
        javaMailSender.send(simpleMailMessage);
    }


    public void sendHtmlMail(MailBean mailBean) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            //邮件发送人
            mimeMessageHelper.setFrom(sender);
            //邮件接收人
            mimeMessageHelper.setTo(mailBean.getRecipient());
            //邮件主题
            mimeMessageHelper.setSubject(mailBean.getSubject());
            //邮件内容
            mimeMessageHelper.setText("<h1>hello</h1>");
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



    public void sendAttachmentMail(MailBean mailBean) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            //邮件发送人
            mimeMessageHelper.setFrom(sender);
            //邮件接收人
            mimeMessageHelper.setTo(mailBean.getRecipient());
            //邮件主题
            mimeMessageHelper.setSubject(mailBean.getSubject());
            //邮件内容
            mimeMessageHelper.setText("<h1>attachment file</h1>");
            FileSystemResource file=new FileSystemResource("src/main/resource/1.png");
            mimeMessageHelper.addAttachment("1.png",file);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



    public void  sendTemplateMail(String name,String recipient,String subject){
        Context context=new Context();
        context.setVariable("name",name);
        String process = templateEngine.process("", context);
        MailBean mailBean=new MailBean();
        mailBean.setContent(process);
        mailBean.setSubject(subject);
        mailBean.setRecipient(recipient);
        sendHtmlMail(mailBean);
    }

}

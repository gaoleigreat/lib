package com.framework.mail.model;
import lombok.Data;
/**
 * @author yanglf
 * @description
 * @since 2019/1/4
 **/
@Data
public class MailBean {
    /**
     * 邮件接收人
     */
    private String recipient;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String content;

}

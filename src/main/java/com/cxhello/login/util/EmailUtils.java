package com.cxhello.login.util;

import com.cxhello.login.exception.BusinessException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * @author cxhello
 * @date 2021/11/1
 */
@Component
public class EmailUtils {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtils.class);

    @Value("${email.hostName}")
    private String hostName;

    @Value("${email.sender}")
    private String sender;

    @Value("${email.senderName}")
    private String senderName;

    @Value("${email.password}")
    private String password;

    @Value("${email.subject}")
    private String subject;

    public String send(String sendTo) {
        StringBuilder sb = new StringBuilder();
        HtmlEmail htmlEmail = new HtmlEmail();
        try {
            htmlEmail.setHostName(hostName);
            htmlEmail.setCharset("UTF-8");
            htmlEmail.addTo(sendTo);
            htmlEmail.setFrom(sender, senderName);
            htmlEmail.setAuthentication(sender, password);
            htmlEmail.setSubject(new String(subject.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            for (int i = 0; i < 6; i++) {
                int number = new Random().nextInt(10);
                sb.append(number);
            }
            htmlEmail.setMsg(String.format("您的账号正在使用邮箱验证，本次请求的验证码为%s，5分钟内有效", sb.toString()));
            htmlEmail.send();
        } catch (Exception e) {
            logger.error("发送邮件失败", e);
            throw new BusinessException("发送邮件失败");
        }
        return sb.toString();
    }

}

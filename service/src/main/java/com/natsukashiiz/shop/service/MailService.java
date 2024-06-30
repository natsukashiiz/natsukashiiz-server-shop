package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.EmailException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Service
@AllArgsConstructor
@Log4j2
public class MailService {

    private JavaMailSender mailSender;
    private MailProperties properties;

    public void send(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendWithHTML(String to, String subject, String html) {
        MimeMessagePreparator message = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(properties.getUsername());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
        };
        mailSender.send(message);
    }

    public void sendActiveUser(String email, String code, String link) throws BaseException {
        String html;

        try {
            html = readEmailTemplate("email-activate-account.html");
        } catch (IOException e) {
            throw EmailException.templateNotFound();
        }

        html = html.replace("${CODE}", code);
        html = html.replace("${LINK}", link);

        String subject = "กรุณาเปิดใช้งานบัญชีของคุณ";

        sendWithHTML(email, subject, html);
    }

    public void sendResetPassword(String email, String link) throws BaseException {
        String html;

        try {
            html = readEmailTemplate("email-reset-password.html");
        } catch (IOException e) {
            throw EmailException.templateNotFound();
        }

        html = html.replace("${LINK}", link);

        String subject = "รีเซ็ตรหัสผ่าน";

        sendWithHTML(email, subject, html);
    }

    public String readEmailTemplate(String fileName) throws IOException {
        File file = ResourceUtils.getFile("classpath:email/" + fileName);
        return FileCopyUtils.copyToString(new FileReader(file));
    }
}

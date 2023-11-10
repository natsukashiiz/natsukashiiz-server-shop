package com.natsukashiiz.shop.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class MailService {

    private JavaMailSender mailSender;

    public void send(String to, String subject, String text) {
        log.debug("Send-[next]. to:{}, subject:{}, text:{}", to, subject, text);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
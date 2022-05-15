package com.example.HRMS.services;

import com.example.HRMS.DTO.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Properties;

@Service
public class  MailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private Environment environment;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setUsername("hrmsystemkg@gmail.com");
        mailSender.setPassword("hrmsystem2022");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        return mailSender;
    }

    public ResponseMessage send(String toEmail, String subject, String text) {
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(text);
            simpleMailMessage.setTo(toEmail);

            simpleMailMessage.setFrom(Objects.requireNonNull(environment.getProperty("spring.mail.username")));
            javaMailSender.send( simpleMailMessage);
            return new ResponseMessage("Сообщение отправлено на почту!", 200);
        }catch(Exception e){
            return new ResponseMessage("Ошибка при отправке смс на почту. " + e.getMessage(), 400);
        }
    }
}
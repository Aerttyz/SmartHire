package com.smarthire.resume.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String remetente;

    @Async
    public CompletableFuture<String> enviarEmailTexto(String destinatario, String assunto, String mensagem) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(remetente);
        simpleMailMessage.setTo(destinatario);
        simpleMailMessage.setSubject(assunto);
        simpleMailMessage.setText(mensagem);
        try {
            javaMailSender.send(simpleMailMessage);
            return CompletableFuture.completedFuture("Email enviado com sucesso!");
        } catch (MailException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}

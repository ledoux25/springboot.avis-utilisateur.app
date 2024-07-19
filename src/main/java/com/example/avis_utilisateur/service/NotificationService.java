package com.example.avis_utilisateur.service;

import com.example.avis_utilisateur.entity.Validation;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationService {
    JavaMailSender javaMailSender;
    public void envoyer(Validation validation){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("no-reply@chillo.tech");
        mailMessage.setTo(validation.getUtilisateur().getEmail());
        mailMessage.setSubject("Votre Code D'activation");
        String text = String.format("Bonjour %s, <br/> votre code d'activation est %s; A bientot", validation.getUtilisateur().getNom(), validation.getCode());
        mailMessage.setText(text);

        javaMailSender.send(mailMessage);
    }
}

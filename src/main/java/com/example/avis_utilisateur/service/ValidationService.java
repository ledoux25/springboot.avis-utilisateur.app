package com.example.avis_utilisateur.service;

import com.example.avis_utilisateur.entity.Utilisateur;
import com.example.avis_utilisateur.entity.Validation;
import com.example.avis_utilisateur.repository.ValidationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@AllArgsConstructor
@Service
public class ValidationService {
    private ValidationRepository validationRepository;
    private NotificationService notificationService;

    public void enregistrer(Utilisateur utilisateur){
        Validation validation = new Validation();
        validation.setUtilisateur(utilisateur);

        Instant creation = Instant.now();
        validation.setCreation(creation);
        Instant expiration = creation.plus(10, ChronoUnit.MINUTES);
        validation.setExpire(expiration);
        Random random= new Random();
        int randoInteger = random.nextInt(999999);
        String code = String.format("%06d",randoInteger);

        validation.setCode(code);
        this.validationRepository.save(validation);
        this.notificationService.envoyer(validation);
    }

    public Validation lireEnFonctionDuCode(String code){
       return this.validationRepository.findByCode(code).orElseThrow( () -> new RuntimeException("Votre code est invalide"));
    }
}

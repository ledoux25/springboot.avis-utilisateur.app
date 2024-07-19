package com.example.avis_utilisateur.service;

import com.example.avis_utilisateur.entity.Avis;
import com.example.avis_utilisateur.repository.AvisRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@AllArgsConstructor
@Service
public class AvisService {

    AvisRepository avisRepository;

    public void creer(Avis avis){
        this.avisRepository.save(avis);
    }

}

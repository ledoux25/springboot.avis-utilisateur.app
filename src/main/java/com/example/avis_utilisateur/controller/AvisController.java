package com.example.avis_utilisateur.controller;

import com.example.avis_utilisateur.entity.Avis;
import com.example.avis_utilisateur.service.AvisService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
@RestController
@RequestMapping(path = "avis", consumes = APPLICATION_JSON_VALUE)
public class AvisController {
    AvisService avisService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void creer(@RequestBody Avis avis){
        this.avisService.creer(avis);
    }
}

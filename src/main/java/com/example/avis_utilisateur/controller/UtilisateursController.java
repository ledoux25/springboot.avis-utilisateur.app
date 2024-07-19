package com.example.avis_utilisateur.controller;

import com.example.avis_utilisateur.dto.AuthentificationDTO;
import com.example.avis_utilisateur.entity.Utilisateur;
import com.example.avis_utilisateur.entity.Validation;
import com.example.avis_utilisateur.security.JwtService;
import com.example.avis_utilisateur.service.UtilisateurService;
import com.example.avis_utilisateur.service.ValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping()
public class UtilisateursController {

    private  UtilisateurService utilisateurService;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private static final Logger log = LoggerFactory.getLogger(UtilisateursController.class);



    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "inscription", consumes = APPLICATION_JSON_VALUE)
    public void inscription(@RequestBody Utilisateur user){
        this.utilisateurService.inscription(user);
    }

    @PostMapping(path = "activation", consumes = APPLICATION_JSON_VALUE)
    public void activation(@RequestBody Map<String, String> activation){
        this.utilisateurService.activation(activation);
    }

    @PostMapping(path = "deconnexion", consumes = APPLICATION_JSON_VALUE)
    public void deconnexion(){
        this.jwtService.deconnexion();
    }

    @PostMapping(path = "connexion")
    public  Map<String, String> connexion(@RequestBody AuthentificationDTO authentificationDTO){
        final Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authentificationDTO.username(), authentificationDTO.password())
        );
        if (authenticate.isAuthenticated()){

           return   this.jwtService.generate(authentificationDTO.username());
        }

        return null;
    }

}

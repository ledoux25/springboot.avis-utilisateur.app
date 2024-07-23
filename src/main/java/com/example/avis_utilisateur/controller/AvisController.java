package com.example.avis_utilisateur.controller;

import com.example.avis_utilisateur.dto.PageRequestDto;
import com.example.avis_utilisateur.entity.Avis;
import com.example.avis_utilisateur.entity.Utilisateur;
import com.example.avis_utilisateur.service.AvisService;
import com.example.avis_utilisateur.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
@RestController
@RequestMapping(path = "avis")
public class AvisController {
    AvisService avisService;
    JwtService jwtService;



    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<Avis> getAvis(@RequestHeader("Authorization") String token){
        Utilisateur user = jwtService.lireUser(token.substring(7));

        return this.avisService.getAvis(user);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, path = ("/pagination/{offset}/{pageSize}"))
    public Page<Avis> getAvisWithPagination(@RequestHeader("Authorization") String token, @PathVariable int offset, @PathVariable int pageSize){
        Utilisateur user = jwtService.lireUser(token.substring(7));
        return avisService.findAvisWithPagination(offset,pageSize);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE,path = "/")
    public List<Avis> getAvisWithSort(@RequestHeader("Authorization") String token, @RequestParam String message){
        Utilisateur user = jwtService.lireUser(token.substring(7));
        return this.avisService.getAvisWithRequest(user, message);
    }



    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "{id:\\d+}")
    public Optional<Avis> getAnAvis(@PathVariable int id, @RequestHeader("Authorization") String token){
        Utilisateur user = jwtService.lireUser(token.substring(7));
        return this.avisService.getAnAvis(id, user);
    }




    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public void creer(@RequestHeader("Authorization") String token, @RequestBody Avis avis ){
        Utilisateur user = jwtService.lireUser(token.substring(7));
        this.avisService.creer(avis, user);
    }


    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteById(@PathVariable int id,@RequestHeader("Authorization") String token){
        Utilisateur user = jwtService.lireUser(token.substring(7));
        this.avisService.deleteById(id,user);
    }

    @DeleteMapping(name = "deletes")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteByIds(@RequestBody List<Integer> ids, @RequestHeader("Authorization") String token){
        Utilisateur user = jwtService.lireUser(token.substring(7));
        for( int id : ids){
            this.avisService.deleteById(id,user);
        }
    }


    @PutMapping(consumes = APPLICATION_JSON_VALUE, name = "update")
    @ResponseStatus(HttpStatus.OK)
    public  void updateAvis(@RequestBody Avis avis,@RequestHeader("Authorization") String token){
        Utilisateur user = jwtService.lireUser(token.substring(7));

        this.avisService.updateAvis(avis,user);
    }
}

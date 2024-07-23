package com.example.avis_utilisateur.service;

import com.example.avis_utilisateur.entity.Avis;
import com.example.avis_utilisateur.entity.Utilisateur;
import com.example.avis_utilisateur.repository.AvisRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class AvisService {

    AvisRepository avisRepository;
    JwtService jwtService;
    TokenService tokenService;


    public void creer(Avis avis, Utilisateur utilisateur){
        avis.setUtilisateur(utilisateur);
        if (utilisateur.getRole().getLibelle().toString() == "ADMINISTRATEUR"){
            throw new RuntimeException("En tant qu'admin vous ne pouvez pas creer d'avis");
        }
        this.avisRepository.save(avis);
    }

    public List<Avis> getAvis(Utilisateur user) {
        if (user.getRole().getLibelle().toString() != "ADMINISTRATEUR"){
            return  this.avisRepository.findAllByUtilisateurAndDeletedAtIsNull(user);
        }

        return this.avisRepository.findAllByDeletedAtIsNull();
    }

    public List<Avis> getAvisWithRequest(Utilisateur user, String message) {
        if (user.getRole().getLibelle().toString() != "ADMINISTRATEUR"){
            return  this.avisRepository.findAllByUtilisateurAndDeletedAtIsNullAndMessageContaining(user,message);
        }
        return this.avisRepository.findAllByDeletedAtIsNullAndMessage(message);
    }

    public  Page<Avis> findAvisWithPagination(int offset, int pageSize){
        return  avisRepository.findAll(PageRequest.of(offset, pageSize));
    }

    public void deleteById(int id, Utilisateur user) {
        Optional<Avis> avis =  this.avisRepository.findById(id);

        String role = user.getRole().getLibelle().toString();

        if(avis.get().getUtilisateur() != user && role != "ADMINISTRATEUR"){
            log.info("le role est :{}",user.getRole().toString());

            throw new RuntimeException("L'avis n'est pas le votre");
        }
        Date deletedAt = Date.from(Instant.now());
        avis.get().setDeletedAt(deletedAt);
        this.avisRepository.save(avis.get());
    }

    public Optional<Avis> getAnAvis(int id,Utilisateur user) {
        if (user.getRole().getLibelle().toString() == "UTILISATEUR"){
            return this.avisRepository.findByIdAndUtilisateurAndDeletedAtIsNull(id,user);
        }
        return this.avisRepository.findByIdAndDeletedAtIsNull(id);
    }

//    @Scheduled(cron = "0 * * * */1 *")
//    public void emptyAvis(){
//        this.avisRepository.deleteAll();
//    }

    public void updateAvis(Avis avis, Utilisateur utilisateur) {
        if (utilisateur.getRole().getLibelle().toString() == "ADMINISTRATEUR"){
            throw new RuntimeException("En tant qu'admin vous ne pouvez pas creer d'avis");
        }
        this.avisRepository.save(avis);
    }
}

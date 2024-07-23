package com.example.avis_utilisateur.service;

import com.example.avis_utilisateur.entity.Role;
import com.example.avis_utilisateur.entity.Utilisateur;
import com.example.avis_utilisateur.entity.Validation;
import com.example.avis_utilisateur.enums.TypeDeRole;
import com.example.avis_utilisateur.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class UtilisateurService implements UserDetailsService {
    private  UtilisateurRepository utilisateurRepository;
    private ValidationService validationService;
    private BCryptPasswordEncoder passwordEncoder;




    public void inscription(Utilisateur user, String role){
        log.info("ya{}",role);

        if (!user.getEmail().contains("@")){
            throw new RuntimeException("Votre adresse mail est incorect");
        }
        if (!user.getEmail().contains(".")){
            throw new RuntimeException("Votre adresse mail est incorect");
        }

        Role roleUtilisate = new Role();

       if (role.equals("administrateur")){
           roleUtilisate.setLibelle(TypeDeRole.ADMINISTRATEUR);

       }else if (role.equals("utilisateur")){
           roleUtilisate.setLibelle(TypeDeRole.UTILISATEUR);


       }

        user.setRole(roleUtilisate);

        Optional<Utilisateur> optionalUtilisateur = this.utilisateurRepository.findByEmail(user.getEmail());
        if (optionalUtilisateur.isPresent()){
            throw new RuntimeException("Votre email est deja utiliser");
        }

        String mdpCrypte = this.passwordEncoder.encode(user.getMdp());
        user.setMdp(mdpCrypte);
        user = this.utilisateurRepository.save(user);

        this.validationService.enregistrer(user);

    }


    public void activation(Map<String, String> activation) {
        Validation validation = this.validationService.lireEnFonctionDuCode(activation.get("code"));

        if (Instant.now().isAfter(validation.getExpire())){
            throw new RuntimeException("Votre code a expirer");
        }
        Utilisateur utilisateur = this.utilisateurRepository.findById(validation.getUtilisateur().getId()).orElseThrow(()-> new RuntimeException("Utilisateur Inconnu"));

        utilisateur.setActif(true);
        this.utilisateurRepository.save(utilisateur);
    }


    @Override
    public Utilisateur loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.utilisateurRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur n'as cette adresse mail"));
    }
}

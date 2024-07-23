package com.example.avis_utilisateur.service;

import com.example.avis_utilisateur.entity.Jwt;
import com.example.avis_utilisateur.entity.Utilisateur;
import com.example.avis_utilisateur.repository.JwtRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@AllArgsConstructor
@Service
public class JwtService {
    public static final String BEARER = "bearer";
    private final String ENCRIPTION_KEY = "4bd1c775f98b2757e6fd35b166ccd3602886e5b26e90498667fee1fafc7c03cf";
    private UtilisateurService utilisateurService;
    private JwtRepository jwtRepository;

    public Map<String, String> generate(String username){
        Utilisateur utilisateur = (Utilisateur) this.utilisateurService.loadUserByUsername(username);
        this.disableTokens(utilisateur);
        Map<String, String> jwtMap = this.generateJwt(utilisateur);
        final Jwt jwt = Jwt
                .builder()
                .valeur(jwtMap.get(BEARER))
                .desactive(false)
                .expire(false)
                .utilisateur(utilisateur)
                .build();
        this.jwtRepository.save(jwt);
        return jwtMap;
    }

    private void disableTokens(Utilisateur utilisateur){
        final List<Jwt> jwtList = this.jwtRepository.findUtilisateur(utilisateur.getEmail()).peek(
                jwt -> {
                    jwt.setDesactive(true);
                    jwt.setExpire(true);
                }
        ).collect(Collectors.toList());
        this.jwtRepository.saveAll(jwtList);
    }

    private Map<String, String> generateJwt(Utilisateur utilisateur) {
        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + 30 * 60 *1000;

        Map<String, Object> claims = Map.of(
                "nom", utilisateur.getNom(),
                "email", utilisateur.getEmail(),
                "role",utilisateur.getRole().getLibelle(),
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT,utilisateur.getEmail()
        );
        String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(utilisateur.getEmail())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of(
                BEARER,
                bearer
        );
    }

    private Key getKey(){
        final byte[] decoder = Decoders.BASE64.decode(ENCRIPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }

    public String  lireUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }



    public Utilisateur  lireUser(String token) {
        return utilisateurService.loadUserByUsername(this.getClaim(token, Claims::getSubject));
    }


    public boolean isTokenExpired(String token) {
        Date expirationDate = this.getClaim(token,Claims::getExpiration);
        return expirationDate.before(new Date());
    }


    private <T> T getClaim(String token, Function<Claims, T> function){
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Jwt tokenByValue(String valeur) {
        return this.jwtRepository.findByValeur(valeur).orElseThrow( ()-> new RuntimeException("Token pas trouver"));
    }

    public void deconnexion() {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = this.jwtRepository.findByUtilisateurValidToken(utilisateur.getEmail(),false,false ).orElseThrow(()-> new RuntimeException("Token inconnu"));
        jwt.setExpire(true);
        jwt.setDesactive(true);
        this.jwtRepository.save(jwt);
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void removeUselessJwt(){
        log.info("Suprssion token {}", Instant.now());
        this.jwtRepository.deleteAllByExpireAndDesactive(true,true);
    }
}

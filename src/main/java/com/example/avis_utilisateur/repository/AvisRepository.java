package com.example.avis_utilisateur.repository;

import com.example.avis_utilisateur.entity.Avis;
import com.example.avis_utilisateur.entity.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AvisRepository extends JpaRepository<Avis, Integer> {


    List<Avis> findAllByUtilisateurAndDeletedAtIsNull(Utilisateur utilisateur);

    List<Avis> findAllByUtilisateurAndDeletedAtIsNullAndMessageContaining(Utilisateur utilisateur, String message);

    List<Avis> findAllByDeletedAtIsNull();


    Optional<Avis> findByIdAndDeletedAtIsNull(Integer integer);

    Optional<Avis> findByIdAndUtilisateurAndDeletedAtIsNull(Integer integer, Utilisateur utilisateur);

    List<Avis> findAllByDeletedAtIsNullAndMessage(String message);

    @Override
    @Query("FROM Avis a WHERE a.deletedAt IS NOT NULL")
    void deleteAll();

}

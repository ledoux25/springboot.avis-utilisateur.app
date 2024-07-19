package com.example.avis_utilisateur.repository;

import com.example.avis_utilisateur.entity.Avis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvisRepository extends JpaRepository<Avis, Integer> {
}

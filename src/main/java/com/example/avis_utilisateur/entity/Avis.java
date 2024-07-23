package com.example.avis_utilisateur.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "avis")
public class Avis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String message;
    private String type;
    @Column(name = "deleted_at")
    private Date deletedAt;
    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
}

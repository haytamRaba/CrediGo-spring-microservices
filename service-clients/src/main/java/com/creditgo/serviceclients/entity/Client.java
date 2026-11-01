package com.creditgo.serviceclients.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull(message = "revenus mensuels sont obligatoires")
    @PositiveOrZero(message = "Les revenus ne peuvent pas être négatifs")
    @Column(nullable = false)
    private Double revenusMensuels;

    @PositiveOrZero()
    @Column(nullable = true)
    private Integer scoreInterne;

    @NotBlank(message = "Le statut est obligatoire")
    @Column(nullable = false)
    private String statut;

    public Client() {
    }

    public Client(
            String nom,
            String email,
            Double revenusMensuels,
            Integer scoreInterne,
            String statut
    ) {
        this.nom = nom;
        this.email = email;
        this.revenusMensuels = revenusMensuels;
        this.scoreInterne = null;
        this.statut = statut;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getRevenusMensuels() {
        return revenusMensuels;
    }

    public void setRevenusMensuels(Double revenusMensuels) {
        this.revenusMensuels = revenusMensuels;
    }

    public Integer getScoreInterne() {
        return scoreInterne;
    }

    public void setScoreInterne(Integer scoreInterne) {
        this.scoreInterne = scoreInterne;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
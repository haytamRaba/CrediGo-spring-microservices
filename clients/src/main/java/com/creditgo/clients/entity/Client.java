package com.creditgo.clients.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

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

    @NotNull(message = "Le revenu mensuel est obligatoire")
    @DecimalMin(value = "0.0", message = "Le revenu mensuel ne peut pas être négatif")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal revenusMensuels;

    @NotNull(message = "Le score interne est obligatoire")
    @Column(nullable = false)
    private Integer scoreInterne;

    @NotNull(message = "Le statut est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClientStatut statut;

    public Client() {
    }

    public Client(String nom, String email, BigDecimal revenusMensuels, Integer scoreInterne, ClientStatut statut) {
        this.nom = nom;
        this.email = email;
        this.revenusMensuels = revenusMensuels;
        this.scoreInterne = scoreInterne;
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

    public BigDecimal getRevenusMensuels() {
        return revenusMensuels;
    }

    public void setRevenusMensuels(BigDecimal revenusMensuels) {
        this.revenusMensuels = revenusMensuels;
    }

    public Integer getScoreInterne() {
        return scoreInterne;
    }

    public void setScoreInterne(Integer scoreInterne) {
        this.scoreInterne = scoreInterne;
    }

    public ClientStatut getStatut() {
        return statut;
    }

    public void setStatut(ClientStatut statut) {
        this.statut = statut;
    }

    public enum ClientStatut {
        ACTIVE, INACTIVE, BLOQUE
    }
}

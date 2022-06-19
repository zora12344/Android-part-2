package com.example.ecole.entities;

public class Etudiant {

    private String nom,prenom;
    private float note;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public String getPrenom() {
        return prenom;
    }

    public float getNote() {
        return note;
    }
}

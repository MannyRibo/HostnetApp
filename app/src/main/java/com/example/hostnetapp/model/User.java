package com.example.hostnetapp.model;

public class User {

    private String naam;
    private String emailadres;

    public User(String naam, String emailadres) {
        this.naam = naam;
        this.emailadres = emailadres;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getEmailadres() {
        return emailadres;
    }

    public void setEmailadres(String emailadres) {
        this.emailadres = emailadres;
    }
}

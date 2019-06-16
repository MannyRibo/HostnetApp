package com.example.hostnetapp.model;

public class User {

    private String naam;
    private String emailadres;
    private String telefoonnummer;

    public User(String naam, String emailadres, String telefoonnummer) {
        this.naam = naam;
        this.emailadres = emailadres;
        this.telefoonnummer = telefoonnummer;
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

    public String getTelefoonnummer() {
        return telefoonnummer;
    }

    public void setTelefoonnummer(String telefoonnummer) {
        this.telefoonnummer = telefoonnummer;
    }
}
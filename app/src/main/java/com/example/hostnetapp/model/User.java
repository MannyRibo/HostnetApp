package com.example.hostnetapp.model;

public class User {

    private String userID;
    private String naam;
    private String emailadres;
    private String telefoonnummer;
    private Rooster rooster;
    private int afdeling;

    public User(String userID, String naam, String emailadres, String telefoonnummer, Rooster rooster, int afdeling) {
        this.userID = userID;
        this.naam = naam;
        this.emailadres = emailadres;
        this.telefoonnummer = telefoonnummer;
        this.rooster = rooster;
        this.afdeling = afdeling;
    }

    public User() {
        //lege contructor nodig
    }

    public String getUserID() { return userID; }

    public void setUserID(String userID) { this.userID = userID; }

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

    public Rooster getRooster() { return rooster; }

    public void setRooster(Rooster rooster) { this.rooster = rooster; }

    public int getAfdeling() {
        return afdeling;
    }

    public void setAfdeling(int afdeling) {
        this.afdeling = afdeling;
    }
}
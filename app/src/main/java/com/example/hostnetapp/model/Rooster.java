package com.example.hostnetapp.model;

public class Rooster {

    private String maandag;
    private String dinsdag;
    private String woensdag;
    private String donderdag;
    private String vrijdag;
    private String zaterdag;

    public Rooster(String maandag, String dinsdag, String woensdag,
                   String donderdag, String vrijdag, String zaterdag, String zondag) {
        this.maandag = maandag;
        this.dinsdag = dinsdag;
        this.woensdag = woensdag;
        this.donderdag = donderdag;
        this.vrijdag = vrijdag;
        this.zaterdag = zaterdag;
        this.zondag = zondag;
    }

    private String zondag;

    public String getMaandag() {
        return maandag;
    }

    public void setMaandag(String maandag) {
        this.maandag = maandag;
    }

    public String getDinsdag() {
        return dinsdag;
    }

    public void setDinsdag(String dinsdag) {
        this.dinsdag = dinsdag;
    }

    public String getWoensdag() {
        return woensdag;
    }

    public void setWoensdag(String woensdag) {
        this.woensdag = woensdag;
    }

    public String getDonderdag() {
        return donderdag;
    }

    public void setDonderdag(String donderdag) {
        this.donderdag = donderdag;
    }

    public String getVrijdag() {
        return vrijdag;
    }

    public void setVrijdag(String vrijdag) {
        this.vrijdag = vrijdag;
    }

    public String getZaterdag() {
        return zaterdag;
    }

    public void setZaterdag(String zaterdag) {
        this.zaterdag = zaterdag;
    }

    public String getZondag() {
        return zondag;
    }

    public void setZondag(String zondag) {
        this.zondag = zondag;
    }
}

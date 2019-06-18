package com.example.hostnetapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Rooster implements Parcelable {

    private String maandag;
    private String dinsdag;
    private String woensdag;
    private String donderdag;
    private String vrijdag;
    private String zaterdag;
    private String zondag;

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

    public Rooster(){
        //lege constructor nodig
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(maandag);
        dest.writeString(dinsdag);
        dest.writeString(woensdag);
        dest.writeString(donderdag);
        dest.writeString(vrijdag);
        dest.writeString(zaterdag);
        dest.writeString(zondag);
    }

    protected Rooster(Parcel in) {
        maandag = in.readString();
        dinsdag = in.readString();
        woensdag = in.readString();
        donderdag = in.readString();
        vrijdag = in.readString();
        zaterdag = in.readString();
        zondag = in.readString();
    }

    public static final Creator<Rooster> CREATOR = new Creator<Rooster>() {
        @Override
        public Rooster createFromParcel(Parcel in) {
            return new Rooster(in);
        }

        @Override
        public Rooster[] newArray(int size) {
            return new Rooster[size];
        }
    };
}

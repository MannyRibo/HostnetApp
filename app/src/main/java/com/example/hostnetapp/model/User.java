package com.example.hostnetapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String userID;
    private String naam;
    private String emailadres;
    private String telefoonnummer;
    private Rooster rooster;
    private String afdeling;
    private String imageurl;

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public User(String userID, String naam, String emailadres, String telefoonnummer, Rooster rooster, String afdeling, String imageurl) {
        this.userID = userID;
        this.naam = naam;
        this.emailadres = emailadres;
        this.telefoonnummer = telefoonnummer;
        this.rooster = rooster;
        this.afdeling = afdeling;
        this.imageurl = imageurl;
    }

    public User() {
        //lege contructor nodig
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public Rooster getRooster() {
        return rooster;
    }

    public void setRooster(Rooster rooster) {
        this.rooster = rooster;
    }

    public String getAfdeling() {
        return afdeling;
    }

    public void setAfdeling(String afdeling) {
        this.afdeling = afdeling;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeString(naam);
        dest.writeString(emailadres);
        dest.writeString(telefoonnummer);
        dest.writeParcelable(rooster, flags);
        dest.writeString(afdeling);
        dest.writeString(imageurl);
    }

    protected User(Parcel in) {
        userID = in.readString();
        naam = in.readString();
        emailadres = in.readString();
        telefoonnummer = in.readString();
        rooster = in.readParcelable(Rooster.class.getClassLoader());
        afdeling = in.readString();
        imageurl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
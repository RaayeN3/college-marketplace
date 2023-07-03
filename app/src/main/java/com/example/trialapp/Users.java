package com.example.trialapp;

public class Users {

    String fullname,phonenumber;

    public Users(String name, String phonenumber) {
        this.fullname = name;
        this.phonenumber = phonenumber;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }
}


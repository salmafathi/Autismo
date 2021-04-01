package com.salma.loginlayout.database;

import java.util.ArrayList;

public class User {
    private String Uid;
    private String name ;
    private String email;
    private String provider;
    private String imageurl;
    private ArrayList<Subcategory> favorites ;

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.provider = password;
    }

    public User(String id, String name, String email, String provider,String imageurl, ArrayList<Subcategory> favorites) {
        this.Uid = id;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.favorites = favorites;
        this.imageurl = imageurl ;
    }

    public  String getImageurl (){return imageurl;}

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        this.Uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public ArrayList<Subcategory> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<Subcategory> favorites) {
        this.favorites = favorites;
    }
}

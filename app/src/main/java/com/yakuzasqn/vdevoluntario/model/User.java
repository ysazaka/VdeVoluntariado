package com.yakuzasqn.vdevoluntario.model;

import com.google.firebase.database.Exclude;
import com.stfalcon.chatkit.commons.models.IUser;

/**
 * Created by yoshi on 14/04/2018.
 */

public class User implements IUser{

    private String id;
    private String name;
    private String email;
    private String password;
    private String picture;
    // Ids dos grupos os quais o usuário pertence
    private long[] groups;

    public User(){}

    public User(String id) {
        this.id = id;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Volunteer
    public User(String id, String name, String email, String password, String picture) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.picture = picture;
    }


    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String getPicture() {
        return picture;
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

    @Exclude
    public String getPassword() {
        return password;
    }

    @Exclude
    public void setPassword(String password) {
        this.password = password;
    }

}



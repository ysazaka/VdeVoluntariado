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
    private byte userType;

    /* Institute exclusive attributes*/
    private String adress;
    private String site;
    private String phone;
    private String category;
    private String[] pictures;
    /**/

    public User(){}

    public User(String id) {
        this.id = id;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Volunteer
    public User(String id, String name, String email, String password, String picture, byte userType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.picture = picture;
        this.userType = userType;
    }

    // Institute
    public User(String id, String name, byte userType, String adress, String site, String phone, String category, String[] pictures) {
        this.id = id;
        this.name = name;
        this.userType = userType;
        this.adress = adress;
        this.site = site;
        this.phone = phone;
        this.category = category;
        this.pictures = pictures;
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

    public byte getUserType() {
        return userType;
    }

    public void setUserType(byte userType) {
        this.userType = userType;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String[] getPictures() {
        return pictures;
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures;
    }
}

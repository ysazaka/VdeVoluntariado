package com.yakuzasqn.vdevoluntario.model;

import java.util.List;

public class Group {
    private String id;
    private String name;
    private String adress;
    private String site;
    private String phone;
    private String category;
    private String picture;
    private List<String> pictures;

    // Ids dos usuários pertencentes ao grupo
    private List<String> participantsId;

    public Group() {}

    public Group(String id, String name, String adress, String site, String phone, String category, String picture, List<String> participantsId) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.site = site;
        this.phone = phone;
        this.category = category;
        this.picture = picture;
        this.participantsId = participantsId;
    }

    public Group(String id, String name, String adress, String site, String phone, String category, List<String> pictures, List<String> participantsId) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.site = site;
        this.phone = phone;
        this.category = category;
        this.pictures = pictures;
        this.participantsId = participantsId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public List<String> getParticipantsId() {
        return participantsId;
    }

    public void setParticipantsId(List<String> participantsId) {
        this.participantsId = participantsId;
    }

}

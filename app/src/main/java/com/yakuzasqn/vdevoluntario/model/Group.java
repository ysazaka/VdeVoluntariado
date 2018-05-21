package com.yakuzasqn.vdevoluntario.model;

import java.util.List;

public class Group {
    private String id;
    private String name;
    private String adress;
    private String site;
    private String phone;
    private String area;
    private String picture;
    private List<String> pictures;

    // Ids dos usuários pertencentes ao grupo
    private List<String> participantsId;

    public Group() {}

    public Group(String id, String name, String adress, String phone, String area, String picture, List<String> participantsId) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.phone = phone;
        this.area = area;
        this.picture = picture;
        this.participantsId = participantsId;
    }

    public Group(String id, String name, String adress, String site, String phone, String area, String picture, List<String> participantsId) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.site = site;
        this.phone = phone;
        this.area = area;
        this.picture = picture;
        this.participantsId = participantsId;
    }

    public Group(String id, String name, String adress, String site, String phone, String area, List<String> pictures, List<String> participantsId) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.site = site;
        this.phone = phone;
        this.area = area;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

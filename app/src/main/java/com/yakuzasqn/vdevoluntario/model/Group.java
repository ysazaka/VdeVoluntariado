package com.yakuzasqn.vdevoluntario.model;

import com.google.firebase.database.Exclude;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.List;

public class Group implements IUser {
    private String id;
    private String name;
    private String adress;
    private String site;
    private String phone;
    private String area;
    private String picture;
    private List<String> pictures;

    // Ids dos usuários pertencentes ao grupo
    private List<String> participantIdList;
    // Ids dos admins
    private String adminId;

    public Group() {}

    public Group(String id, String name, String adress, String phone, String area, String picture, List<String> participantIdList) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.phone = phone;
        this.area = area;
        this.picture = picture;
        this.participantIdList = participantIdList;
    }

    public Group(String id, String name, String adress, String site, String phone, String area, String picture, List<String> participantIdList) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.site = site;
        this.phone = phone;
        this.area = area;
        this.picture = picture;
        this.participantIdList = participantIdList;
    }

    public Group(String id, String name, String adress, String site, String phone, String area, List<String> pictures, List<String> participantIdList) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.site = site;
        this.phone = phone;
        this.area = area;
        this.pictures = pictures;
        this.participantIdList = participantIdList;
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

    @Override
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Exclude
    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    @Exclude
    public List<String> getPictures() {
        return pictures;
    }

    public List<String> getParticipantIdList() {
        return participantIdList;
    }

    public void setParticipantIdList(List<String> participantIdList) {
        this.participantIdList = participantIdList;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
}

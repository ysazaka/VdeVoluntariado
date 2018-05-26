package com.yakuzasqn.vdevoluntario.model;

import java.util.Date;

public class Chat {

    private String message;
    private User chosenUser;
    private Group chosenGroup;
    private Date createdAt;

    public Chat() {}


    public User getChosenUser() {
        return chosenUser;
    }

    public void setChosenUser(User chosenUser) {
        this.chosenUser = chosenUser;
    }

    public Group getChosenGroup() {
        return chosenGroup;
    }

    public void setChosenGroup(Group chosenGroup) {
        this.chosenGroup = chosenGroup;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

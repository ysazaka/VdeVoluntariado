package com.yakuzasqn.vdevoluntario.model;

import java.util.Date;

public class Chat {

    private String message;
    private User chosenUser;
    private Date createdAt;

    public Chat() {}


    public User getChosenUser() {
        return chosenUser;
    }

    public void setChosenUser(User chosenUser) {
        this.chosenUser = chosenUser;
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

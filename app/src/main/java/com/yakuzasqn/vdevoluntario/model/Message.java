package com.yakuzasqn.vdevoluntario.model;

import com.google.firebase.database.Exclude;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.io.File;
import java.util.Date;

public class Message implements IMessage{

    /* IMessage required attributes */
    private String id;
    private String message;
    private Date createdAt;
    private User userChatWith;

    private String userID;

    public Message() {}

    public Message(String userID, String message, Date createdAt, User userChatWith) {
        this.userID = userID;
        this.message = message;
        this.createdAt = createdAt;
        this.userChatWith = userChatWith;
    }

    /** Getters */

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return message;
    }

    @Exclude
    @Override
    public IUser getUser() {
        return new User(String.valueOf(userID));
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    public String getUserID() {
        return userID;
    }

    public User getUserChatWith() {
        return userChatWith;
    }

    /** Setters */

    public void setId(String id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setText(String message) {
        this.message = message;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUserChatWith(User userChatWith) {
        this.userChatWith = userChatWith;
    }

}

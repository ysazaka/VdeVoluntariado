package com.yakuzasqn.vdevoluntario.model;

import com.google.firebase.database.Exclude;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.io.File;
import java.util.Date;

public class Message implements IMessage, MessageContentType.Image{

    /* IMessage required attributes */
    private String id;
    private String message;
    private long createdAt;
    private User userChatWith;

    private String userID;

    private String toUserID;

    private String media;

    private long updatedAt;

    private long requestID;

    private File file;


    public Message() {}


    public Message(String s, String message, User user) {
        this.userID = s;
        this.message = message;
        this.userChatWith = user;
    }

    public Message(String userID, String message, long createdAt) {
        this.userID = userID;
        this.message = message;
        this.createdAt = createdAt;
    }

    public Message(String s, String message, long date, User user) {
        this.userID = s;
        this.message = message;
        this.createdAt = date;
        this.userChatWith = user;
    }

    public Message(String s, File file, long date, User user) {
        this.userID = s;
        this.file = file;
        this.createdAt = date;
        this.userChatWith = user;
    }

    public Message(String id, String message, long createdAt, long updatedAt, long requestID) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.requestID = requestID;
    }

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

    @Exclude
    @Override
    public Date getCreatedAt() {
        return new Date(createdAt);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(createdAt * 1000);
//
//        return calendar.getTime();
    }

    public void setCreatedAt(Date date) {
        this.createdAt = date.getTime();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setText(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Exclude
    public User getUserChatWith() {
        return userChatWith;
    }

    public void setUserChatWith(User userChatWith) {
        this.userChatWith = userChatWith;
    }

    public String getToUserID() {
        return toUserID;
    }

    @Exclude
    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    @Exclude
    public long getRequestID() {
        return requestID;
    }

    public void setRequestID(long requestID) {
        this.requestID = requestID;
    }

    @Override
    public String getImageUrl() {
        return media;
    }
}

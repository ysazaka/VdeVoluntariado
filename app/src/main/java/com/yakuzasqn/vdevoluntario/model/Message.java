package com.yakuzasqn.vdevoluntario.model;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.io.File;
import java.util.Calendar;
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

    public Message() {
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

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return message;
    }

    @Override
    public IUser getUser() {
        return new User(String.valueOf(userID));
    }

    @Override
    public Date getCreatedAt() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(createdAt * 1000);

        return calendar.getTime();
    }

    public User getUserFromMesage(){
        return userChatWith;
    }

    public void setText(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public User getUserChatWith() {
        return userChatWith;
    }

    public void setUserChatWith(User userChatWith) {
        this.userChatWith = userChatWith;
    }

    public String getToUserID() {
        return toUserID;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

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

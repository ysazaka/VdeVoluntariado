package com.yakuzasqn.vdevoluntario.model;

public class Post {
    private String id;
    private String urlImage;
    private String title;
    private String description;
    private String type;

    private User user;
    private Group group;

    public Post() {
    }

    public Post(String id, String urlImage, String title, String description, String type, User user) {
        this.id = id;
        this.urlImage = urlImage;
        this.title = title;
        this.description = description;
        this.type = type;
        this.user = user;
    }

    public Post(String id, String urlImage, String title, String description, String type, Group group) {
        this.id = id;
        this.urlImage = urlImage;
        this.title = title;
        this.description = description;
        this.type = type;
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}

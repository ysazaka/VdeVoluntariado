package com.yakuzasqn.vdevoluntario.model;

public class Post {
    private String urlImage;
    private String title;
    private String description;

    private User user;

    public Post() {
    }

    public Post(String urlImage, String title, String description, User user) {
        this.urlImage = urlImage;
        this.title = title;
        this.description = description;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}

package com.pawhub.model;

public class User {
    private String email;
    private String username;
    private String profile_picture;
    private Integer post_count;

    public User(String email, String username, String profile_picture, Integer post_count) {
        this.email = email;
        this.username = username;
        this.profile_picture = profile_picture;
        this.post_count = post_count;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public Integer getPost_count() {
        return post_count;
    }

    public void setPost_count(Integer post_count) {
        this.post_count = post_count;
    }
}

package com.pawhub.model;

import com.google.firebase.Timestamp;

public class Post {
    private String post_id;
    private String uid;
    private Timestamp date_posted;
    private String image;
    private Integer like_count;

    public Post() {}

    public Post(String post_id, String uid, Timestamp date_posted, String image, Integer like_count) {
        this.post_id = post_id;
        this.uid = uid;
        this.date_posted = date_posted;
        this.image = image;
        this.like_count = like_count;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Timestamp getDate_posted() {
        return date_posted;
    }

    public void setDate_posted(Timestamp date_posted) {
        this.date_posted = date_posted;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getLike_count() {
        return like_count;
    }

    public void setLike_count(Integer like_count) {
        this.like_count = like_count;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
}

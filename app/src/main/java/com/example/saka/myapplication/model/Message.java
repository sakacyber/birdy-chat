package com.example.saka.myapplication.model;

/**
 * Created by Saka on 26-May-17.
 */

public class Message {

    private String id;
    private String message;
    private String senderUid;
    private String timeStamp;
    private String photoUrl;
    private String imageUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String name) {
        this.senderUid = name;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String dateTime) {
        this.timeStamp = dateTime;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

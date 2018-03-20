package com.example.saka.myapplication.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Saka on 02-May-17.
 */

public class User {

    private String uid;
    private String name;
    private String email;
    private int friendCount = 0;
    private int requestToCount = 0;
    private int requestFromCount = 0;
    private Map<String, Object> requestTo = new HashMap<>();
    private Map<String, Object> friend = new HashMap<>();
    private Map<String, Object> requestFrom = new HashMap<>();

    public User() {
        //  Default constructor
    }

    public User(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("name", name);
        map.put("email", email);
        map.put("friendCount", friendCount);
        map.put("requestFromCount", requestFromCount);
        return map;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
    }

    public int getRequestFromCount() {
        return requestFromCount;
    }

    public void setRequestFromCount(int requestFromCount) {
        this.requestFromCount = requestFromCount;
    }

    public Map<String, Object> getFriend() {
        return friend;
    }

    public void setFriend(Map<String, Object> friends) {
        this.friend = friends;
    }

    public Map<String, Object> getRequestFrom() {
        return requestFrom;
    }

    public void setRequestFrom(Map<String, Object> requestFrom) {
        this.requestFrom = requestFrom;
    }

    public int getRequestToCount() {
        return requestToCount;
    }

    public void setRequestToCount(int requestToCount) {
        this.requestToCount = requestToCount;
    }

    public Map<String, Object> getRequestTo() {
        return requestTo;
    }

    public void setRequestTo(Map<String, Object> requestTo) {
        this.requestTo = requestTo;
    }
}

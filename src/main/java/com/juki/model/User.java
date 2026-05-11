package com.juki.model;

public class User {
    private int id;
    private String fullName;
    private String username;
    private String profileImagePath;

    public User(int id, String fullName, String username) {
        this(id, fullName, username, null);
    }

    public User(int id, String fullName, String username, String profileImagePath) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.profileImagePath = profileImagePath;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }
}
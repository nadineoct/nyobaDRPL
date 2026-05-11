package com.juki.model;

public class UserSession {
    private static UserSession instance;
    private User activeUser;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setActiveUser(User user) {
        this.activeUser = user;
    }

    public User getActiveUser() {
        return this.activeUser;
    }

    public void clearSession() {
        this.activeUser = null;
    }
}
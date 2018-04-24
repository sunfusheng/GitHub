package com.sunfusheng.github.model;

/**
 * @author sunfusheng on 2018/4/24.
 */
public class Contribution {

    private String title;
    private String username;

    public Contribution(String title, String username) {
        this.title = title;
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

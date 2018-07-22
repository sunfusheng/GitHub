package com.sunfusheng.github.model;

import java.util.List;

/**
 * @author sunfusheng on 2018/7/13.
 */
public class Issue {
    public String url;
    public String repository_url;
    public String labels_url;
    public String comments_url;
    public String events_url;
    public String html_url;
    public int id;
    public String node_id;
    public int number;
    public String title;
    public User user;
    public String state;
    public boolean locked;
    public int comments;
    public String created_at;
    public String updated_at;
    public String closed_at;
    public String author_association;
    public List<Label> labels;
    public String body;

    public Repo repo;
}

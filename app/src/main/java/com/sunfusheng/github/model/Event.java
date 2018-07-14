package com.sunfusheng.github.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author sunfusheng on 2018/5/7.
 */
public class Event {
    public static final String IssueCommentEvent = "IssueCommentEvent";
    public static final String PullRequestEvent = "PullRequestEvent";
    public static final String IssuesEvent = "IssuesEvent";
    public static final String WatchEvent = "WatchEvent";
    public static final String ForkEvent = "ForkEvent";
//    public static final String IssueCommentEvent = "IssueCommentEvent";
//    public static final String IssueCommentEvent = "IssueCommentEvent";
//    public static final String IssueCommentEvent = "IssueCommentEvent";
//    public static final String IssueCommentEvent = "IssueCommentEvent";

    public String id;
    public String type;
    public Actor actor;
    public Repo repo;
    public Payload payload;
    @SerializedName("public")
    public boolean publicX;
    public String created_at;

    public String getType() {
        return type;
    }
}

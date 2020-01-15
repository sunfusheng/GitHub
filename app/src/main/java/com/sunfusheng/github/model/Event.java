package com.sunfusheng.github.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.sunfusheng.github.util.Utils;

/**
 * @author sunfusheng on 2018/5/7.
 */
public class Event implements Comparable<Event> {
    public static final String WatchEvent = "WatchEvent";
    public static final String ForkEvent = "ForkEvent";
    public static final String PushEvent = "PushEvent";
    public static final String CreateEvent = "CreateEvent";
    public static final String DeleteEvent = "DeleteEvent";
    public static final String IssueCommentEvent = "IssueCommentEvent";
    public static final String IssuesEvent = "IssuesEvent";
    public static final String PullRequestEvent = "PullRequestEvent";

    public String id;
    public String type;
    public User actor;
    public Repo repo;
    public Payload payload;
    @SerializedName("public")
    public boolean publicX;
    public String created_at;

    public String getType() {
        return type;
    }

    @Override
    public int compareTo(@NonNull Event o) {
        long result = Utils.getMilliSeconds(o.created_at) - Utils.getMilliSeconds(this.created_at);
        if (result > 0) {
            return 1;
        } else if (result < 0) {
            return -1;
        }
        return 0;
    }
}

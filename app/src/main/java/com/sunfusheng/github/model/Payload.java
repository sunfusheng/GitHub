package com.sunfusheng.github.model;

import java.util.List;

/**
 * @author sunfusheng on 2018/7/13.
 */
public class Payload {
    public String action;
    public int number;
    public Issue issue;
    public Comment comment;
    public Repo forkee;
    public PullRequest pull_request;
    public List<Commit> commits;

    // CreateEvent
    public String ref;
    public String ref_type;
    public String master_branch;
    public String description;
    public String pusher_type;
}

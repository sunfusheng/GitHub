package com.sunfusheng.github.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * @author sunfusheng on 2018/4/23.
 */
@Entity(indices = {@Index(value = {"owner_name", "name", "full_name"}, unique = true)})
public class Repo {

    @PrimaryKey
    public int id;
    public String name;
    public String full_name;

    @Ignore
    public User owner;
    public String owner_name;

    @SerializedName("private")
    public boolean privateX;

    public String html_url;
    public String description;
    public boolean fork;
    public String url;
    public String forks_url;
    public String keys_url;
    public String collaborators_url;
    public String teams_url;
    public String hooks_url;
    public String issue_events_url;
    public String events_url;
    public String assignees_url;
    public String branches_url;
    public String tags_url;
    public String blobs_url;
    public String git_tags_url;
    public String git_refs_url;
    public String trees_url;
    public String statuses_url;
    public String languages_url;
    public String stargazers_url;
    public String contributors_url;
    public String subscribers_url;
    public String subscription_url;
    public String commits_url;
    public String git_commits_url;
    public String comments_url;
    public String issue_comment_url;
    public String contents_url;
    public String compare_url;
    public String merges_url;
    public String archive_url;
    public String downloads_url;
    public String issues_url;
    public String pulls_url;
    public String milestones_url;
    public String notifications_url;
    public String labels_url;
    public String releases_url;
    public String deployments_url;
    public String created_at;
    public String updated_at;
    public String pushed_at;
    public String git_url;
    public String ssh_url;
    public String clone_url;
    public String svn_url;
    public String homepage;
    public int size;
    public int stargazers_count;
    public int watchers_count;
    public String language;
    public boolean has_issues;
    public boolean has_projects;
    public boolean has_downloads;
    public boolean has_wiki;
    public boolean has_pages;
    public int forks_count;
    public String mirror_url;
    public boolean archived;
    public int open_issues_count;
    public int forks;
    public int open_issues;
    public int watchers;
    public String default_branch;

    @Embedded
    public License license;

    public static class License {
        @ColumnInfo(name = "license_key")
        public String key;
        @ColumnInfo(name = "license_name")
        public String name;
        @ColumnInfo(name = "license_spdx_id")
        public String spdx_id;
        @ColumnInfo(name = "license_url")
        public String url;

    }

    @Override
    public String toString() {
        return "Repo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", full_name='" + full_name + '\'' +
                ", owner=" + owner +
                ", owner_name='" + owner_name + '\'' +
                ", privateX=" + privateX +
                ", html_url='" + html_url + '\'' +
                ", description='" + description + '\'' +
                ", fork=" + fork +
                ", url='" + url + '\'' +
                ", forks_url='" + forks_url + '\'' +
                ", keys_url='" + keys_url + '\'' +
                ", collaborators_url='" + collaborators_url + '\'' +
                ", teams_url='" + teams_url + '\'' +
                ", hooks_url='" + hooks_url + '\'' +
                ", issue_events_url='" + issue_events_url + '\'' +
                ", events_url='" + events_url + '\'' +
                ", assignees_url='" + assignees_url + '\'' +
                ", branches_url='" + branches_url + '\'' +
                ", tags_url='" + tags_url + '\'' +
                ", blobs_url='" + blobs_url + '\'' +
                ", git_tags_url='" + git_tags_url + '\'' +
                ", git_refs_url='" + git_refs_url + '\'' +
                ", trees_url='" + trees_url + '\'' +
                ", statuses_url='" + statuses_url + '\'' +
                ", languages_url='" + languages_url + '\'' +
                ", stargazers_url='" + stargazers_url + '\'' +
                ", contributors_url='" + contributors_url + '\'' +
                ", subscribers_url='" + subscribers_url + '\'' +
                ", subscription_url='" + subscription_url + '\'' +
                ", commits_url='" + commits_url + '\'' +
                ", git_commits_url='" + git_commits_url + '\'' +
                ", comments_url='" + comments_url + '\'' +
                ", issue_comment_url='" + issue_comment_url + '\'' +
                ", contents_url='" + contents_url + '\'' +
                ", compare_url='" + compare_url + '\'' +
                ", merges_url='" + merges_url + '\'' +
                ", archive_url='" + archive_url + '\'' +
                ", downloads_url='" + downloads_url + '\'' +
                ", issues_url='" + issues_url + '\'' +
                ", pulls_url='" + pulls_url + '\'' +
                ", milestones_url='" + milestones_url + '\'' +
                ", notifications_url='" + notifications_url + '\'' +
                ", labels_url='" + labels_url + '\'' +
                ", releases_url='" + releases_url + '\'' +
                ", deployments_url='" + deployments_url + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", pushed_at='" + pushed_at + '\'' +
                ", git_url='" + git_url + '\'' +
                ", ssh_url='" + ssh_url + '\'' +
                ", clone_url='" + clone_url + '\'' +
                ", svn_url='" + svn_url + '\'' +
                ", homepage='" + homepage + '\'' +
                ", size=" + size +
                ", stargazers_count=" + stargazers_count +
                ", watchers_count=" + watchers_count +
                ", language='" + language + '\'' +
                ", has_issues=" + has_issues +
                ", has_projects=" + has_projects +
                ", has_downloads=" + has_downloads +
                ", has_wiki=" + has_wiki +
                ", has_pages=" + has_pages +
                ", forks_count=" + forks_count +
                ", mirror_url='" + mirror_url + '\'' +
                ", archived=" + archived +
                ", open_issues_count=" + open_issues_count +
                ", forks=" + forks +
                ", open_issues=" + open_issues +
                ", watchers=" + watchers +
                ", default_branch='" + default_branch + '\'' +
                ", license=" + license +
                '}';
    }
}

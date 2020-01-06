package com.sunfusheng.github.util.readme;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * @author by sunfusheng on 2019-05-22
 */
public class GitHubInfo {
    private String url;
    private String userName;
    private String repoName;

    public static GitHubInfo fromUrl(@NonNull String url) {
        if (!GitHubHelper.isGitHubUrl(url)) return null;
        GitHubInfo gitHubName = new GitHubInfo();
        url = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
        gitHubName.url = url;
        try {
            Uri uri = Uri.parse(url);
            ArrayList<String> list = new ArrayList<>(uri.getPathSegments());
            list.remove("repos");
            if (list.size() > 0) gitHubName.userName = list.get(0);
            if (list.size() > 1) gitHubName.repoName = list.get(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gitHubName;
    }

    public String getUserName() {
        return userName;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getReleaseTagName() {
        if (!GitHubHelper.isReleaseTagUrl(url)) {
            return null;
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public String getCommitShaName() {
        if (!GitHubHelper.isCommitUrl(url)) {
            return null;
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }
}

package com.sunfusheng.github.util;

import android.graphics.Color;
import android.text.SpannableString;

import com.sunfusheng.github.R;
import com.sunfusheng.github.model.Comment;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.model.Issue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author sunfusheng on 2018/4/23.
 */
public class Utils {
    public static final String GITHUB_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static int getColorByLanguage(String language) {
        switch (language) {
            case "Java":
                return Color.parseColor("#af7126");
            case "Objective-C":
                return Color.parseColor("#4791fc");
            case "JavaScript":
                return Color.parseColor("#efde6d");
            case "C++":
                return Color.parseColor("#ef517f");
            case "Swift":
                return Color.parseColor("#fbaa59");
            case "HTML":
                return Color.parseColor("#df4e37");
            case "Python":
                return Color.parseColor("#3873a3");
            case "Go":
                return Color.parseColor("#3960a9");
            case "Kotlin":
                return Color.parseColor("#ef8d3f");
            case "Dart":
                return Color.parseColor("#1cb4bb");
            case "Groovy":
                return Color.parseColor("#e59e5c");
            case "PHP":
                return Color.parseColor("#4f5e93");
        }
        return Color.parseColor("#666666");
    }

    public static String getStarCount(int starCount) {
        if (starCount > 1000) {
            float result = (starCount / 100) * 1f / 10 + (starCount % 100 >= 50 ? 0.1f : 0f);
            return String.format(Locale.getDefault(), "%.1f", result) + "k";
        }
        return String.valueOf(starCount);
    }

    public static long getMilliSeconds(String dateStr) {
        return DateUtil.getMilliSeconds(dateStr, GITHUB_PATTERN) + DateUtil.BEIJING_SECOND;
    }

    public static String getDate(String dateStr) {
        return DateUtil.formatDate(getMilliSeconds(dateStr));
    }

    public static String getDateAgo(String dateStr) {
        return DateUtil.formatTimeAgo(new Date().getTime() - getMilliSeconds(dateStr), true);
    }

    public static SpannableString getEventDesc(Event item) {
        StringBuilder sb = new StringBuilder();
        List<String> tags = new ArrayList<>();
        sb.append(item.actor.login).append(" ");
        tags.add(item.actor.login);
        if (item.type.equals(Event.WatchEvent)) {
            sb.append("starred ").append(item.repo.full_name);
            tags.add(item.repo.full_name);
        } else {
            sb.append("forked ").append(item.payload.forkee.full_name).append(" from ").append(item.repo.full_name);
            tags.add(item.payload.forkee.full_name);
            tags.add(item.repo.full_name);
        }
        sb.append(" ").append(Utils.getDateAgo(item.created_at));
        return SpannableUtil.getSpannableString(sb.toString(), R.color.font_event, tags);
    }

    public static SpannableString getCommentDesc(Event item) {
        StringBuilder sb = new StringBuilder();
        List<String> tags = new ArrayList<>();
        Comment comment = item.payload.comment;
        String issueNumber = item.repo.name + "#" + item.payload.issue.number;

        sb.append(comment.user.login)
                .append(" commented on issue ")
                .append(issueNumber).append(" ")
                .append(Utils.getDateAgo(item.created_at));

        tags.add(comment.user.login);
        tags.add(issueNumber);
        return SpannableUtil.getSpannableString(sb.toString(), R.color.font_event, tags);
    }

    public static SpannableString getIssueDesc(Event item) {
        StringBuilder sb = new StringBuilder();
        List<String> tags = new ArrayList<>();
        Issue issue = item.payload.issue;
        String issueNumber = item.repo.name + "#" + issue.number;
        String actor = issue.user.login;
        if (item.type.equals(Event.IssuesEvent)) {
            actor = item.actor.login;
        }

        sb.append(actor).append(" ").
                append(item.payload.action).append(" issue ")
                .append(issueNumber).append(" ")
                .append(Utils.getDateAgo(item.created_at));

        tags.add(actor);
        tags.add(issueNumber);
        return SpannableUtil.getSpannableString(sb.toString(), R.color.font_event, tags);
    }

}

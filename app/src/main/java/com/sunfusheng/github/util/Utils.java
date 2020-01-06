package com.sunfusheng.github.util;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.ColorRes;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.model.Issue;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.ui.NavigationManager;
import com.sunfusheng.github.widget.span.TouchableSpan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.functions.Action;

/**
 * @author sunfusheng on 2018/4/23.
 */
public class Utils {
    public static final String GITHUB_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    // Keywords
    public static final String STARRED = "starred";
    public static final String FORKED = "forked";
    public static final String FROM = "from";
    public static final String COMMENT_ISSUE = "commented on issue";
    public static final String ISSUE = "issue";

    public static int getColorByLanguage(String language) {
        switch (language) {
            case "Java":
                return Color.parseColor("#b07219");
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

    public static String getCountDesc(int count, boolean showExactNum) {
        if (!showExactNum && count > 1000) {
            float result = count / 1000 + (count % 1000 / 100) * 1f / 10 + (count % 1000 % 100 >= 50 ? 0.1f : 0f);
            return String.format(Locale.getDefault(), "%.1f", result) + "k";
        }
        return String.valueOf(count);
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

    public static String getUserName(String repoFullName) {
        if (!TextUtils.isEmpty(repoFullName)) {
            String[] split = repoFullName.split("/");
            if (split.length < 2) {
                return null;
            }
            return split[0];
        }
        return null;
    }

    public static String getRepoName(String repoFullName) {
        if (!TextUtils.isEmpty(repoFullName)) {
            String[] split = repoFullName.split("/");
            if (split.length < 2) {
                return null;
            }
            return split[1];
        }
        return null;
    }

    public static String getUsernameDesc(User user) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(user.name)) {
            sb.append(user.name);
        }
        if (!TextUtils.isEmpty(sb)) {
            sb.append("（").append(user.login).append("）");
        } else {
            sb.append(user.login);
        }
        return sb.toString();
    }

    public static String getCreatedTimeDesc(String createdTime) {
        return "Joined in " + DateUtil.formatDate2String(createdTime, DateUtil.FORMAT.format(DateUtil.FORMAT.yyyyMMdd));
    }

    public static boolean isMyRepo(Repo repo) {
        if (repo != null && repo.owner != null && !TextUtils.isEmpty(repo.owner.login)) {
            return repo.owner.login.equals(PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME, ""));
        }
        return false;
    }

    public static boolean isMyIssue(Issue issue) {
        return isMyRepo(issue.repo);
    }

    public static void refinedSpannableString(String desc, SpannableString sp, String tag, @ColorRes int textColor, @ColorRes int pressedBackgroundColor, Action action) {
        if (TextUtils.isEmpty(desc) || TextUtils.isEmpty(sp) || TextUtils.isEmpty(tag)) return;
        int index;
        int start = 0;
        int end;
        while ((index = desc.indexOf(tag, start)) > -1) {
            end = index + tag.length();
            sp.setSpan(new TouchableSpan(AppUtil.getContext(), textColor, textColor, R.color.transparent, pressedBackgroundColor) {
                @Override
                public void onSpanClick(View widget) {
                    if (action != null) {
                        try {
                            action.run();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, index, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            start = end;
        }
    }

    public static void refinedSpannableString(String desc, SpannableString sp, String tag, @ColorRes int textColor, Action action) {
        refinedSpannableString(desc, sp, tag, textColor, R.color.background_common_darker, action);
    }

    public static void refinedUserSpannableString(Context context, String desc, SpannableString sp, User user) {
        refinedSpannableString(desc, sp, user.login, R.color.font_highlight, () -> {
            NavigationManager.toUserActivity(context, user);

        });
    }

    public static void refinedRepoSpannableString(Context context, String desc, SpannableString sp, Repo repo) {
        refinedSpannableString(desc, sp, repo.full_name, R.color.font_highlight, () -> {
            NavigationManager.toRepoDetailActivity(context, repo.full_name);
        });
    }

    public static void refinedIssueSpannableString(String desc, SpannableString sp, Issue issue) {
        String label = issue.repo.full_name + '#' + issue.number;
        refinedSpannableString(desc, sp, label, R.color.font_highlight, () -> {
            Toast.makeText(AppUtil.getContext(), label, Toast.LENGTH_SHORT).show();
        });
    }

    public static void refinedKeywordSpannableString(String desc, SpannableString sp, String keywords) {
        refinedSpannableString(desc, sp, keywords, R.color.font_keyword, R.color.transparent, null);
    }

    public static SpannableString getDescSpannableString(Context context, String desc, List<User> users, List<Repo> repos, List<Issue> issues, List<String> keywords) {
        SpannableString spannableString = new SpannableString(desc);
        if (!CollectionUtil.isEmpty(users)) {
            for (User user : users) {
                refinedUserSpannableString(context, desc, spannableString, user);
            }
        }

        if (!CollectionUtil.isEmpty(repos)) {
            for (Repo repo : repos) {
                refinedRepoSpannableString(context, desc, spannableString, repo);
            }
        }

        if (!CollectionUtil.isEmpty(issues)) {
            for (Issue issue : issues) {
                refinedIssueSpannableString(desc, spannableString, issue);
            }
        }

        if (!CollectionUtil.isEmpty(keywords)) {
            for (String keyword : keywords) {
                refinedKeywordSpannableString(desc, spannableString, keyword);
            }
        }
        return spannableString;
    }

    public static SpannableString getWatchForkDesc(Context context, Event item) {
        StringBuilder desc = new StringBuilder();
        List<User> users = new ArrayList<>();
        List<Repo> repos = new ArrayList<>();
        List<String> keywords = new ArrayList<>();

        desc.append(item.actor.login).append(' ');
        users.add(item.actor);
        if (item.type.equals(Event.WatchEvent)) {
            desc.append(STARRED).append(' ').append(item.repo.full_name);
            keywords.add(STARRED);
            repos.add(item.repo);
        } else {
            desc.append(FORKED).append(' ')
                    .append(item.payload.forkee.full_name).append(' ')
                    .append(FROM).append(' ')
                    .append(item.repo.full_name);
            keywords.add(FORKED);
            keywords.add(FROM);
            repos.add(item.payload.forkee);
            repos.add(item.repo);
        }
        desc.append(' ').append(Utils.getDateAgo(item.created_at));

        return getDescSpannableString(context, desc.toString(), users, repos, null, keywords);
    }

    public static SpannableString getCommentIssueDesc(Context context, Event item) {
        StringBuilder desc = new StringBuilder();
        List<User> users = new ArrayList<>();
        List<Issue> issues = new ArrayList<>();
        List<String> keywords = new ArrayList<>();
        String issueNumber = item.repo.full_name + '#' + item.payload.issue.number;

        desc.append(item.payload.comment.user.login).append(' ')
                .append(COMMENT_ISSUE).append(' ')
                .append(issueNumber).append(' ')
                .append(Utils.getDateAgo(item.created_at));

        users.add(item.payload.comment.user);
        item.payload.issue.repo = item.repo;
        issues.add(item.payload.issue);
        keywords.add(COMMENT_ISSUE);

        return getDescSpannableString(context, desc.toString(), users, null, issues, keywords);
    }

    public static SpannableString getIssueDesc(Context context, Event item) {
        StringBuilder desc = new StringBuilder();
        List<User> users = new ArrayList<>();
        List<Issue> issues = new ArrayList<>();
        List<String> keywords = new ArrayList<>();
        User user = item.payload.issue.user;
        if (item.type.equals(Event.IssuesEvent)) {
            user = item.actor;
        }
        String issueNumber = item.repo.full_name + '#' + item.payload.issue.number;

        desc.append(user.login).append(' ')
                .append(item.payload.action).append(' ')
                .append(ISSUE).append(' ')
                .append(issueNumber).append(' ')
                .append(Utils.getDateAgo(item.created_at));

        users.add(user);
        item.payload.issue.repo = item.repo;
        issues.add(item.payload.issue);
        keywords.add(item.payload.action);
        keywords.add(ISSUE);

        return getDescSpannableString(context, desc.toString(), users, null, issues, keywords);
    }

}

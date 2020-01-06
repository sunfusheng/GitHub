package com.sunfusheng.github.util.readme;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author by sunfusheng on 2019-05-22
 */
public class ReadmeHtmlUtil {

    private static Pattern LINK_PATTERN = Pattern.compile("href=\"(.*?)\"");
    private static Pattern IMAGE_PATTERN = Pattern.compile("src=\"(.*?)\"");

    public static String generateMdHtml(@NonNull String mdSource, @Nullable String baseUrl,
                                        boolean isDark, @NonNull String backgroundColor,
                                        @NonNull String accentColor, boolean wrapCode) {
        String skin = isDark ? "markdown_dark.css" : "markdown_white.css";
        mdSource = TextUtils.isEmpty(baseUrl) ? mdSource : fixLinks(mdSource, baseUrl);
        //fix wiki inner url like this "href="/robbyrussell/oh-my-zsh/wiki/Themes""
        mdSource = fixWikiLinks(mdSource);
        return generateMdHtml(mdSource, skin, backgroundColor, accentColor, wrapCode);
    }

    private static String generateMdHtml(@NonNull String mdSource, String skin,
                                         @NonNull String backgroundColor,
                                         @NonNull String accentColor, boolean wrapCode) {
        return "<html>\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\" />\n" +
                "<title>MD View</title>\n" +
                "<meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;\"/>" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"./" + skin + "\">\n" +
                "<style>" +
                "body{background: " + backgroundColor + ";}" +
                "a {color:" + accentColor + " !important;}" +
                ".highlight pre, pre {" +
                " word-wrap: " + (wrapCode ? "break-word" : "normal") + "; " +
                " white-space: " + (wrapCode ? "pre-wrap" : "pre") + "; " +
                "}" +
                "</style>" +
                "</head>\n" +
                "<body>\n" +
                mdSource +
                "</body>\n" +
                "</html>";
    }

    private static String fixLinks(@NonNull String source, @NonNull String baseUrl) {
        GitHubInfo gitHubName = GitHubInfo.fromUrl(baseUrl);
        if (gitHubName == null) return source;
        String owner = gitHubName.getUserName();
        String repo = gitHubName.getRepoName();
        String branch = baseUrl.substring(baseUrl.indexOf("blob") + 5, baseUrl.lastIndexOf("/"));

        Matcher linksMatcher = LINK_PATTERN.matcher(source);
        while (linksMatcher.find()) {
            String oriUrl = linksMatcher.group(1);
            if (oriUrl.contains("http://") || oriUrl.contains("https://")
                    || oriUrl.startsWith("#") //filter markdown inner link
            ) {
                continue;
            }

            String subUrl = oriUrl.startsWith("/") ? oriUrl : "/".concat(oriUrl);
            String fixedUrl;
            if (!GitHubHelper.isImage(oriUrl)) {
                fixedUrl = "https://github.com/" + owner + "/" + repo + "/blob/" + branch + subUrl;
            } else {
                //if link url is a image
                fixedUrl = "https://raw.githubusercontent.com/" + owner + "/" + repo + "/" + branch + subUrl;
            }
            source = source.replace("href=\"" + oriUrl + "\"", "href=\"" + fixedUrl + "\"");
        }

        Matcher imagesMatcher = IMAGE_PATTERN.matcher(source);
        while (imagesMatcher.find()) {
            String oriUrl = imagesMatcher.group(1);
            if (oriUrl.contains("http://") || oriUrl.contains("https://")) {
                continue;
            }

            String subUrl = oriUrl.startsWith("/") ? oriUrl : "/".concat(oriUrl);
            String fixedUrl = "https://raw.githubusercontent.com/" + owner + "/" + repo + "/" + branch + subUrl;
            source = source.replace("src=\"" + oriUrl + "\"", "src=\"" + fixedUrl + "\"");
        }

        return source;
    }

    private static String fixWikiLinks(@NonNull String source) {
        Matcher linksMatcher = LINK_PATTERN.matcher(source);
        while (linksMatcher.find()) {
            while (linksMatcher.find()) {
                String oriUrl = linksMatcher.group(1);
                String fixedUrl;
                if (oriUrl.startsWith("/") && oriUrl.contains("/wiki/")) {
                    fixedUrl = "https://github.com" + oriUrl;
                    source = source.replace("href=\"" + oriUrl + "\"", "href=\"" + fixedUrl + "\"");
                }
            }
        }
        return source;
    }
}

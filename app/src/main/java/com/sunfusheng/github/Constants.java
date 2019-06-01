package com.sunfusheng.github;

import android.os.Environment;

import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.github.util.SdCardUtil;

import java.io.File;

/**
 * @author sunfusheng on 2018/4/8.
 */
public class Constants {

    public static final String BASE_URL_API_GITHUB = "https://api.github.com/";
    public static final String BASE_URL_GITHUB = "https://github.com/";

    public static final String USER_AGENT = "sunfusheng";
    public static final String ACCEPT_JSON = "application/vnd.github.v3+json";
    public static final String[] SCOPES = {"user", "repo", "notifications", "gist", "admin:org"};
    public static final String NOTE = "DroidGitHub";
    public static final String NOTE_URL = "https://github.com/sunfusheng/GitHub";
    public static final String CLIENT_ID = "0af4dd82697eaea821d5";
    public static final String CLIENT_SECRET = "2d7abc27a2812e3927257a7ae3274475a0505907";

    public static final int PAGE_COUNT = 30;
    public static final int _10_MINUTES = 600;

    public static boolean isReceivedEventsRefreshTimeExpired() {
        long lastRefreshTime = PreferenceUtil.getInstance().getLong(Constants.PreferenceKey.RECEIVED_EVENTS_REFRESH_TIME, -1);
        return lastRefreshTime == -1 || ((System.currentTimeMillis() - lastRefreshTime) / 1000) > Constants._10_MINUTES;
    }

    public static class PreferenceName {
        public static final String SETTINGS = "preference_name_settings";
    }

    public static class PreferenceKey {
        public static final String USERNAME = "preference_key_username";
        public static final String PASSWORD = "preference_key_password";
        public static final String AUTH = "preference_key_auth";
        public static final String TOKEN = "preference_key_token";

        public static final String RECEIVED_EVENTS_REFRESH_TIME = "received_events_refresh_time";
    }

    public static class Bundle {
        public static final String USERNAME = "bundle_username";
        public static final String REPO_NAME = "bundle_repo_name";
        public static final String REPO_FULL_NAME = "bundle_repo_full_name";
    }

    public static class FileDir {
        public static final String ROOT_DIR = Environment.getExternalStorageDirectory() + File.separator + "GitHub" + File.separator;
        public static final String CACHE = ROOT_DIR + "cache" + File.separator;
        public static final String IMAGE = ROOT_DIR + "image" + File.separator;
        public static final String FILE = ROOT_DIR + "file" + File.separator;
    }

    public static class CacheDir {
        public static final File OKHTTP = SdCardUtil.getDiskCacheDir("okhttp");
        public static final File CONTRIBUTION = SdCardUtil.getDiskCacheDir("contribution");
        public static final File README = SdCardUtil.getDiskCacheDir("readme");
    }
}

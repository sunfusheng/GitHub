package com.sunfusheng.github;

import android.os.Environment;

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


    public static class Time {
        public static final int MINUTES_1 = 60;
        public static final int MINUTES_5 = 60 * 5;
        public static final int MINUTES_10 = 60 * 10;
        public static final int MINUTES_20 = 60 * 20;
        public static final int MINUTES_30 = 60 * 30;
        public static final int MINUTES_60 = 60 * 60;
    }

    public static class PreferenceName {
        public static final String SETTINGS = "preference_name_settings";
    }

    public static class PreferenceKey {
        public static final String USERNAME = "preference_key_username";
        public static final String AUTH = "preference_key_auth";
        public static final String TOKEN = "preference_key_token";
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

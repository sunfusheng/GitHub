package com.sunfusheng.github;

import android.os.Environment;

import java.io.File;

/**
 * @author sunfusheng on 2018/4/8.
 */
public class Constants {

    public static final String BASE_URL = "https://api.github.com/";

    public static final String ACCEPT = "application/vnd.github.v3+json";
    public static final String[] SCOPES = {"user", "repo", "notifications", "gist", "admin:org"};
    public static final String NOTE = "DroidGitHub";
    public static final String NOTE_URL = "https://github.com/sfsheng0322/DroidGitHub";
    public static final String CLIENT_ID = "0af4dd82697eaea821d5";
    public static final String CLIENT_SECRET = "2d7abc27a2812e3927257a7ae3274475a0505907";

    public static final int PER_PAGE_10 = 10;
    public static final int PER_PAGE_20 = 20;
    public static final int PER_PAGE_30 = 30;

    public static class PreferenceName {
        public static final String SETTINGS = "preference_name_settings";
    }

    public static class PreferenceKey {
        public static final String USERNAME = "preference_key_username";
        public static final String PASSWORD = "preference_key_password";
        public static final String AUTH = "preference_key_auth";
        public static final String TOKEN = "preference_key_token";
    }

    public static class Bundle {
        public static final String USERNAME = "bundle_username";
    }

    public static class FileDir {
        public static final String ROOT_DIR = Environment.getExternalStorageDirectory() + File.separator + "GitHub" + File.separator;
        public static final String CACHE = ROOT_DIR + "cache" + File.separator;
        public static final String IMAGE = ROOT_DIR + "image" + File.separator;
        public static final String FILE = ROOT_DIR + "file" + File.separator;
        public static final String CONTRIBUTIONS = ROOT_DIR + "contributions" + File.separator;
    }
}

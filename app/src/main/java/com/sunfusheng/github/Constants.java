package com.sunfusheng.github;

/**
 * @author sunfusheng on 2018/4/8.
 */
public class Constants {

    public static final String BASE_URL = "https://api.github.com/";

    public static final String[] SCOPES = {"user", "repo", "notifications", "gist", "admin:org"};
    public static final String NOTE = "DroidGitHub";
    public static final String NOTE_URL = "https://github.com/sfsheng0322/DroidGitHub";
    public static final String CLIENT_ID = "0af4dd82697eaea821d5";
    public static final String CLIENT_SECRET = "2d7abc27a2812e3927257a7ae3274475a0505907";

    public static class PreferenceName {
        public static final String SETTINGS = "preference_name_settings";
    }

    public static class PreferenceKey {
        public static final String USERNAME = "preference_key_username";
        public static final String PASSWORD = "preference_key_password";
        public static final String AUTH = "preference_key_auth";
        public static final String TOKEN = "preference_key_token";
    }
}

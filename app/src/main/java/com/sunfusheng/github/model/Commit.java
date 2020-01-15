package com.sunfusheng.github.model;

/**
 * @author sunfusheng
 * @since 2020-01-15
 */
public class Commit {
    public String sha;
    public AuthorBean author;
    public String message;
    public boolean distinct;
    public String url;

    public static class AuthorBean {
        public String email;
        public String name;
    }
}

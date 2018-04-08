package com.sunfusheng.github.model;

import java.util.List;

/**
 * @author sunfusheng on 2018/4/8.
 */
public class AuthResponse {


    /**
     * id : 178283287
     * url : https://api.github.com/authorizations/178283287
     * app : {"name":"DroidGitHub","url":"https://github.com/sfsheng0322/DroidGitHub","client_id":"0af4dd82697eaea821d5"}
     * token :
     * hashed_token : cbb53ae63acef5435e13f9494b53c3058bda297c33c949a33e0069b0de22f7aa
     * token_last_eight : e31c9dae
     * note : null
     * note_url : null
     * created_at : 2018-04-05T11:24:07Z
     * updated_at : 2018-04-05T11:26:25Z
     * scopes : []
     * fingerprint : null
     */

    private int id;
    private String url;
    private AppEntity app;
    private String token;
    private String hashed_token;
    private String token_last_eight;
    private Object note;
    private Object note_url;
    private String created_at;
    private String updated_at;
    private Object fingerprint;
    private List<?> scopes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AppEntity getApp() {
        return app;
    }

    public void setApp(AppEntity app) {
        this.app = app;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHashed_token() {
        return hashed_token;
    }

    public void setHashed_token(String hashed_token) {
        this.hashed_token = hashed_token;
    }

    public String getToken_last_eight() {
        return token_last_eight;
    }

    public void setToken_last_eight(String token_last_eight) {
        this.token_last_eight = token_last_eight;
    }

    public Object getNote() {
        return note;
    }

    public void setNote(Object note) {
        this.note = note;
    }

    public Object getNote_url() {
        return note_url;
    }

    public void setNote_url(Object note_url) {
        this.note_url = note_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Object getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(Object fingerprint) {
        this.fingerprint = fingerprint;
    }

    public List<?> getScopes() {
        return scopes;
    }

    public void setScopes(List<?> scopes) {
        this.scopes = scopes;
    }

    public static class AppEntity {
        /**
         * name : DroidGitHub
         * url : https://github.com/sfsheng0322/DroidGitHub
         * client_id : 0af4dd82697eaea821d5
         */

        private String name;
        private String url;
        private String client_id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getClient_id() {
            return client_id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", app=" + app +
                ", token='" + token + '\'' +
                ", hashed_token='" + hashed_token + '\'' +
                ", token_last_eight='" + token_last_eight + '\'' +
                ", note=" + note +
                ", note_url=" + note_url +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", fingerprint=" + fingerprint +
                ", scopes=" + scopes +
                '}';
    }
}

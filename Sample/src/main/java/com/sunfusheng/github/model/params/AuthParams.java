package com.sunfusheng.github.model.params;

import com.sunfusheng.github.Constants;

/**
 * @author sunfusheng on 2018/4/8.
 */
public class AuthParams {

    public String[] scopes;
    public String note;
    public String note_url;
    public String client_id;
    public String client_secret;
    public String fingerprint;

    public static AuthParams getParams() {
        AuthParams params = new AuthParams();
        params.note = Constants.NOTE;
        params.note_url = Constants.NOTE_URL;
        params.client_id = Constants.CLIENT_ID;
        params.client_secret = Constants.CLIENT_SECRET;
        params.scopes = Constants.SCOPES;
        return params;
    }
}

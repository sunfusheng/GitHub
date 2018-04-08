package com.sunfusheng.github.net;

import com.sunfusheng.github.model.AuthParams;
import com.sunfusheng.github.model.AuthResponse;
import com.sunfusheng.github.model.User;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author by sunfusheng on 2017/1/17.
 *
 * https://developer.github.com/v3/oauth_authorizations/#create-a-new-authorization
 */
public interface ApiService {

    @GET("user")
    Observable<User> fetchUser(@Header("Authorization") String authorization);

    @POST("authorizations")
    Observable<AuthResponse> createAuth(@Body AuthParams authParams);

    @GET("user")
    Observable<User> getUserInfo(@Query("access_token") String accessToken);

}

package com.sunfusheng.github.net;

import com.sunfusheng.github.model.AuthParams;
import com.sunfusheng.github.model.AuthResponse;
import com.sunfusheng.github.model.User;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author by sunfusheng on 2018/4/8.
 */
public interface ApiService {

    @GET("user")
    Observable<User> fetchUser(@Header("Authorization") String authorization);

    @POST("authorizations")
    Observable<AuthResponse> createAuth(@Body AuthParams authParams);

    @GET("users/{userName}")
    Observable<User> getUserInfo(@Path("userName") String userName);

}

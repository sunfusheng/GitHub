package com.sunfusheng.github.net;

import com.sunfusheng.github.model.Auth;
import com.sunfusheng.github.model.AuthParams;
import com.sunfusheng.github.model.User;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author by sunfusheng on 2018/4/8.
 */
public interface ApiService {

    @GET("user")
    Observable<User> login();

    @POST("authorizations")
    Observable<Auth> createAuth(@Body AuthParams authParams);

    @GET("users/{userName}")
    Observable<Response<User>> fetchUser(@Path("userName") String userName);

}

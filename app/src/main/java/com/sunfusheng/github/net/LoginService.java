package com.sunfusheng.github.net;

import com.sunfusheng.github.model.Auth;
import com.sunfusheng.github.model.AuthParams;
import com.sunfusheng.github.model.User;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author by sunfusheng on 2019-05-31
 */
public interface LoginService {

    @GET("user")
    Observable<User> fetchUser();

    @POST("authorizations")
    Observable<Auth> fetchToken(@Body AuthParams authParams);
}

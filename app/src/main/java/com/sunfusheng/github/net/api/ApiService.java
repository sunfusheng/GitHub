package com.sunfusheng.github.net.api;

import com.sunfusheng.github.model.Auth;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.model.params.AuthParams;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author by sunfusheng on 2018/4/8.
 */
public interface ApiService {

    @GET("user")
    Observable<User> login();

    @POST("authorizations")
    Observable<Auth> createAuth(@Body AuthParams authParams);

    @GET("users/{username}")
    Observable<Response<User>> fetchUser(@Path("username") String username);

    @GET("users/{username}/repos")
    Observable<Response<List<Repo>>> fetchRepos(@Path("username") String username,
                                                @Query("page") int page,
                                                @Query("per_page") int per_page,
                                                @Query("sort") String sort);

    @GET("users/{username}/events")
    Observable<Response<List<Event>>> fetchEvents(@Path("username") String username,
                                                 @Query("page") int page,
                                                 @Query("per_page") int per_page);

}

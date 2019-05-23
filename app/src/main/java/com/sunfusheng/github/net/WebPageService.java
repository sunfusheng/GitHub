package com.sunfusheng.github.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * @author by sunfusheng on 2019/1/24
 */
public interface WebPageService {

    @GET("trending/java")
    Observable<ResponseBody> fetchTendingRepos(
            @Query("since") String since
    );

    @GET
    @Headers("Accept: application/vnd.github.html")
    Observable<Response<ResponseBody>> getFileAsHtmlStream(
            @Url String url
    );
}

package com.sunfusheng.github.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author by sunfusheng on 2019/1/24
 */
public interface WebPageService {

    @GET("trending/java")
    Observable<ResponseBody> fetchTendingRepos(
            @Query("since") String since
    );
}

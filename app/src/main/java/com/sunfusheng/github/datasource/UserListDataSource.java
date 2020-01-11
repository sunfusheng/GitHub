package com.sunfusheng.github.datasource;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.http.Api;
import com.sunfusheng.github.http.response.ResponseData;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.util.CollectionUtil;
import com.sunfusheng.github.viewmodel.params.UsernamePageParams;
import com.sunfusheng.multistate.LoadingState;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * @author sunfusheng
 * @since 2020-01-11
 */
public class UserListDataSource extends BaseDataSource<UsernamePageParams, List<User>> {
    public static final String FROM_FOLLOWER = "follower_list";
    public static final String FROM_FOLLOWING = "following_list";

    private String from;

    private String mUsername;
    private int mPage;
    private int mPageCount;
    private int mFetchMode;

    public UserListDataSource(String from) {
        this.from = from;
    }

    @Override
    public void setParams(UsernamePageParams params) {
        this.mUsername = params.username;
        this.mPage = params.page;
        this.mPageCount = params.pageCount;
        this.mFetchMode = params.fetchMode;
    }

    @Override
    public Observable<ResponseData<List<User>>> localObservable() {
        return Observable.empty();
    }

    @Override
    public Observable<ResponseData<List<User>>> remoteObservable() {
        Observable<Response<List<User>>> observable;
        if (FROM_FOLLOWER.equals(from)) {
            observable = Api.getCommonService().fetchFollowerList(mUsername, mPage, mPageCount, mFetchMode, Constants.Time.MINUTES_60);
        } else if (FROM_FOLLOWING.equals(from)) {
            observable = Api.getCommonService().fetchFollowingList(mUsername, mPage, mPageCount, mFetchMode, Constants.Time.MINUTES_60);
        } else {
            throw new IllegalArgumentException("The parameter of \"from\" is not set!");
        }

        return observable
                .subscribeOn(Schedulers.io())
                .compose(DataSourceHelper.applyRemoteTransformer())
                .doOnNext(it -> {
                    if (DataSourceHelper.isSuccess(it)) {
                        List<User> data = it.data;
                        if (CollectionUtil.isEmpty(data)) {
                            it.setLoadingState(LoadingState.EMPTY);
                        }
                    }
                });
    }
}

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

    private String mFrom;
    private UsernamePageParams mParams;

    public UserListDataSource(String from) {
        this.mFrom = from;
    }

    @Override
    public void setParams(UsernamePageParams params) {
        this.mParams = params;
    }

    @Override
    public Observable<ResponseData<List<User>>> localObservable() {
        return Observable.empty();
    }

    @Override
    public Observable<ResponseData<List<User>>> remoteObservable() {
        Observable<Response<List<User>>> observable;
        if (FROM_FOLLOWER.equals(mFrom)) {
            observable = Api.getCommonService().fetchFollowerList(mParams.username, mParams.page, mParams.pageCount, mParams.fetchMode, Constants.Time.MINUTES_60);
        } else if (FROM_FOLLOWING.equals(mFrom)) {
            observable = Api.getCommonService().fetchFollowingList(mParams.username, mParams.page, mParams.pageCount, mParams.fetchMode, Constants.Time.MINUTES_60);
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

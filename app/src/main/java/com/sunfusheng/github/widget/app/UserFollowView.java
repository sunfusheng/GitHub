package com.sunfusheng.github.widget.app;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.datasource.DataSourceHelper;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.net.response.ResponseObserver;
import com.sunfusheng.github.net.response.ResponseResult;
import com.sunfusheng.github.util.ExceptionUtil;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.multistate.LoadingState;
import com.sunfusheng.wrapper.LoadingView;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/7/28.
 */
public class UserFollowView extends RelativeLayout {

    private TextView vFollow;
    private LoadingView vLoading;

    private String username;
    private boolean hasFollowed;

    public UserFollowView(Context context) {
        this(context, null);
    }

    public UserFollowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserFollowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_user_follow, this);
        vFollow = findViewById(R.id.follow);
        vLoading = findViewById(R.id.loading);

        vFollow.setVisibility(GONE);
        vLoading.setVisibility(GONE);

        vFollow.setOnClickListener(v -> {
            if (hasFollowed) {
                Api.getCommonService().unfollow(username).subscribeOn(Schedulers.io())
                        .compose(DataSourceHelper.applyRemoteTransformer())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxLifecycleAndroid.bindView(this))
                        .subscribe(new ResponseObserver<Boolean>() {
                            @Override
                            public void onNotify(ResponseResult<Boolean> result) {
                                handleView(result, true);
                            }
                        });
            } else {
                Api.getCommonService().follow(username).subscribeOn(Schedulers.io())
                        .compose(DataSourceHelper.applyRemoteTransformer())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxLifecycleAndroid.bindView(this))
                        .subscribe(new ResponseObserver<Boolean>() {
                            @Override
                            public void onNotify(ResponseResult<Boolean> result) {
                                handleView(result, false);
                            }
                        });
            }
        });
    }

    public void setUsername(String username) {
        this.username = username;
        if (TextUtils.isEmpty(username) || username.equals(PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME))) {
            return;
        }

        Api.getCommonService().fetchFollowed(username)
                .subscribeOn(Schedulers.io())
                .compose(DataSourceHelper.applyRemoteTransformer())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleAndroid.bindView(this))
                .subscribe(new ResponseObserver<Boolean>() {
                    @Override
                    public void onNotify(ResponseResult<Boolean> result) {
                        handleView(result, false);
                    }
                });
    }

    private void handleView(ResponseResult<Boolean> result, boolean isReverse) {
        if (result.loadingState == LoadingState.LOADING) {
            vFollow.setEnabled(false);
            vFollow.setVisibility(INVISIBLE);
            vLoading.setVisibility(VISIBLE);
        } else {
            vLoading.setVisibility(GONE);
            if (result.code == ExceptionUtil.NO_CONTENT) {
                hasFollowed = !isReverse;
                vFollow.setEnabled(true);
                vFollow.setVisibility(VISIBLE);
            } else if (result.code == ExceptionUtil.NOT_FOUND) {
                hasFollowed = isReverse;
                vFollow.setEnabled(true);
                vFollow.setVisibility(VISIBLE);
            } else {
                vFollow.setEnabled(false);
                vFollow.setVisibility(GONE);
            }
            vFollow.setText(hasFollowed ? "Unfollow" : "Follow");
        }
    }
}

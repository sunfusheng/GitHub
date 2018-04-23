package com.sunfusheng.github.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunfusheng.GroupRecyclerViewAdapter;
import com.sunfusheng.GroupViewHolder;
import com.sunfusheng.StickyHeaderDecoration;
import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.annotation.LoadingState;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.util.DateUtil;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.github.util.StatusBarUtil;
import com.sunfusheng.github.viewmodel.UserViewModel;
import com.sunfusheng.github.viewmodel.VM;
import com.sunfusheng.glideimageview.GlideImageView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class HomeFragment extends BaseFragment {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private GlideImageView avatarBg;
    private GlideImageView avatar;
    private TextView vInfo;
    private TextView vTitle;
    private TextView vSubtitle;
    private AppBarLayout appBarLayout;
    private RecyclerView recyclerView;

    private String username;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), 0, null);

        if (TextUtils.isEmpty(username)) {
            username = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME);
        }

        userViewModel = VM.of(this, UserViewModel.class);
        userViewModel.setRequestParams(username, FetchMode.DEFAULT);

        avatarBg = view.findViewById(R.id.avatar_bg);
        avatar = view.findViewById(R.id.avatar);
        collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbarLayout);
        toolbar = view.findViewById(R.id.toolbar);
        vInfo = view.findViewById(R.id.info);
        vTitle = view.findViewById(R.id.title);
        vSubtitle = view.findViewById(R.id.subtitle);
        appBarLayout = view.findViewById(R.id.appBarLayout);
        recyclerView = view.findViewById(R.id.recyclerView);

        collapsingToolbarLayout.setTitle("");
        toolbar.setTitle("");
//        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));//设置还没收缩时状态下字体颜色
//        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.transparent));//设置收缩后Toolbar上字体的颜色
//        appBarLayout.addOnOffsetChangedListener((appBar, offset) -> vTitle.setAlpha(Math.abs(offset * 1f / appBar.getTotalScrollRange())));

        userViewModel.liveData.observe(this, it -> {
            if (it.loadingState == LoadingState.SUCCESS) {
                User user = it.data;

                avatar.loadImage(user.getAvatar_url(), R.color.background_common);
                vTitle.setText(user.getName() + "（" + user.getLogin() + "）");
                vSubtitle.setText("创建于" + DateUtil.convertString2String(user.getCreated_at()));
                vInfo.setText("签名: " + user.getBio() + "\n" +
                        "公司: " + user.getCompany() + "\n" +
                        "位置: " + user.getLocation() + "\n" +
                        "博客: " + user.getBlog() + "\n" +
                        "地址: " + user.getHtml_url());

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.addItemDecoration(new StickyHeaderDecoration());
                StickyGroupAdapter stickyAdapter = new StickyGroupAdapter(getContext(), items);
                recyclerView.setAdapter(stickyAdapter);

            }
        });
    }

    public class StickyGroupAdapter extends GroupRecyclerViewAdapter<String> {

        public StickyGroupAdapter(Context context, String[][] groups) {
            super(context, groups);
        }

        @Override
        public boolean showFooter() {
            return false;
        }

        @Override
        public int getHeaderLayoutId(int viewType) {
            return R.layout.item_header_layout;
        }

        @Override
        public int getChildLayoutId(int viewType) {
            return R.layout.item_child_layout;
        }

        @Override
        public int getFooterLayoutId(int viewType) {
            return 0;
        }

        @Override
        public void onBindHeaderViewHolder(GroupViewHolder holder, String item, int groupPosition) {
            holder.setText(R.id.tv_title, item);
        }

        @Override
        public void onBindChildViewHolder(GroupViewHolder holder, String item, int groupPosition, int childPosition) {
            holder.setText(R.id.tv_title, item);
        }

        @Override
        public void onBindFooterViewHolder(GroupViewHolder holder, String item, int groupPosition) {

        }
    }

    public static String[][] items = {
            {"第一组", "1"},
            {"第二组", "1", "2"},
            {"第三组", "1", "2", "3"},
            {"第四组", "1", "2", "3", "4"},
            {"第五组", "1", "2", "3", "4", "5"},
            {"第六组", "1", "2", "3", "4", "5", "6"},
            {"第七组", "1", "2", "3", "4", "5", "6", "7"},
            {"第八组", "1", "2", "3", "4", "5", "6", "7", "8"},
    };

    long start;
    long end;

    private void initView() {

        Observable<String> localObservable = local(2000);
        Observable<String> remoteObservable = remote(2000);

        remoteObservable.publish(remote -> Observable.merge(remote, localObservable.takeUntil(remote)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        start = System.currentTimeMillis();
                        Log.d("--->", "onSubscribe()");
                    }

                    @Override
                    public void onNext(String s) {
                        end = System.currentTimeMillis();
                        Log.d("--->", "onNext() " + s + " : " + (end - start));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("--->", "onError()");
                    }

                    @Override
                    public void onComplete() {
                        end = System.currentTimeMillis();
                        Log.d("--->", "onComplete()" + " : " + (end - start));
                    }
                });
    }

    public Observable<String> local(long ms) {
        return Observable.defer(() -> Observable.just("local"))
                .doOnNext(s -> {
                    end = System.currentTimeMillis();
                    Log.d("--->", "doOnNext() " + s + " : " + (end - start));
                })
                .subscribeOn(Schedulers.io())
                .map(s -> {
                    SystemClock.sleep(ms);
                    return s + " sleep:" + ms;
                });
    }

    public Observable<String> remote(long ms) {
        return Observable.defer(() -> Observable.just("remote"))
                .doOnNext(s -> {
                    end = System.currentTimeMillis();
                    Log.d("--->", "doOnNext() " + s + " : " + (end - start));
                })
                .subscribeOn(Schedulers.io())
                .map(s -> {
                    SystemClock.sleep(ms);
                    return s + " sleep:" + ms;
                });
    }

}

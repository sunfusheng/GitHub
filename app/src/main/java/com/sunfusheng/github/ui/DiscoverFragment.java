package com.sunfusheng.github.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.annotation.LoadingState;
import com.sunfusheng.github.datasource.RemoteDataSource;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.net.ResponseObserver;
import com.sunfusheng.github.net.ResponseResult;
import com.sunfusheng.github.util.DateUtil;
import com.sunfusheng.github.util.DisplayUtil;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.github.util.StatusBarUtil;
import com.sunfusheng.github.viewbinder.RepoViewBinder;
import com.sunfusheng.github.viewbinder.UserProfileViewBinder;
import com.sunfusheng.github.viewmodel.UserViewModel;
import com.sunfusheng.github.viewmodel.VM;
import com.sunfusheng.github.widget.ListenerNestedScrollView;
import com.sunfusheng.glideimageview.GlideImageView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class DiscoverFragment extends BaseFragment {

    private ListenerNestedScrollView nestedScrollView;
    private RecyclerView recyclerView;
    private MultiTypeAdapter adapter;

    private GlideImageView toolbarBg;
    private Toolbar toolbar;

    private String username;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), 0, null);
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private List<Object> items = new ArrayList<>();
    private boolean hasAdded;

    private void initView() {
        View view = getView();
        if (view == null) {
            return;
        }

        toolbarBg = view.findViewById(R.id.toolbar_bg);
        toolbar = view.findViewById(R.id.toolbar);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        recyclerView = view.findViewById(R.id.recyclerView);

        if (TextUtils.isEmpty(username)) {
            username = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME);
        }

        initToolbar();

        userViewModel = VM.of(this, UserViewModel.class);
        userViewModel.setRequestParams(username, FetchMode.DEFAULT);
        userViewModel.liveData.observe(this, it -> {
            if (it.loadingState == LoadingState.SUCCESS && !hasAdded) {
                User user = it.data;
                hasAdded = true;

                toolbar.setTitle(user.getName() + "（" + user.getLogin() + "）");
                toolbar.setSubtitle("创建于" + DateUtil.convertString2String(user.getCreated_at()));
                items.add(0, user);
                adapter.notifyDataSetChanged();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new MultiTypeAdapter();
        recyclerView.setAdapter(adapter);
        adapter.register(Repo.class, new RepoViewBinder());
        adapter.register(User.class, new UserProfileViewBinder());

        Api.getCommonService().fetchRepos("sfsheng0322", "pushed")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RemoteDataSource.applyRemoteTransformer())
                .subscribe(new ResponseObserver<List<Repo>>() {
                    @Override
                    public void onNotify(ResponseResult<List<Repo>> result) {
                        if (result.loadingState == LoadingState.SUCCESS) {
                            items.addAll(result.data);
                            adapter.setItems(items);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void initToolbar() {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) toolbarBg.getLayoutParams();
        int toolbarAndStatusBarHeight = toolbar.getLayoutParams().height + StatusBarUtil.getStatusBarHeight(getContext());
        int height = layoutParams.height - toolbarAndStatusBarHeight;
        Log.d("--->", "height: " + height);
        layoutParams.setMargins(0, -height, 0, 0);
        toolbarBg.setAlpha(0);

        int distance = DisplayUtil.dp2px(getContext(), 220) - toolbarAndStatusBarHeight;
        Log.d("--->", "distance: " + distance);

        nestedScrollView.setOnScrollChangedInterface((scrollX, scrollY, oldScrollX, oldScrollY) -> {
            Log.d("--->", "scrollY: " + scrollY + " oldScrollY: " + oldScrollY);

            if (scrollY < 0) {
                scrollY = 0;
            }
            float alpha = Math.abs(scrollY) * 1.0f / (distance);
            Log.d("--->", "alpha: " + alpha);
            if (scrollY <= distance) {
                toolbarBg.setAlpha((int) (alpha * 255));
            } else {
                toolbarBg.setAlpha(255);
            }
        });
    }

}

package com.sunfusheng.github.ui.repo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.ui.base.BaseFragment;
import com.sunfusheng.github.util.StatusBarUtil;
import com.sunfusheng.github.util.Utils;
import com.sunfusheng.github.widget.app.ReadMeWebView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author by sunfusheng on 2018/11/14
 */
public class RepoDetailFragment extends BaseFragment {

    private ReadMeWebView vReadMe;

    private String repoName;
    private String username;
    private String repoFullName;

    public static RepoDetailFragment instance(String repoFullName) {
        RepoDetailFragment fragment = new RepoDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Bundle.REPO_FULL_NAME, repoFullName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repo_detail, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), 0, null);
        super.onViewCreated(view, savedInstanceState);
        initView();
        initReadmeView();
    }

    private void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            repoFullName = arguments.getString(Constants.Bundle.REPO_FULL_NAME);
        }
        username = Utils.getUserName(repoFullName);
        repoName = Utils.getRepoName(repoFullName);

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(repoName)) {
            throw new IllegalArgumentException("please set repoFullName");
        }
    }

    private void initView() {
        toolbar.setTitle(repoName);
        vReadMe = getView().findViewById(R.id.vReadMe);
    }

    private void initReadmeView() {
        String url = "https://api.github.com/repos/" + repoFullName + "/readme";
        String baseUrl = "https://github.com/" + repoFullName + "/blob/master/README.md";

        Disposable disposable = Api.getWebPageService().getFileAsHtmlStream(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(it -> {
                    vReadMe.setMdSource(it.body().string(), baseUrl, false);
                }, Throwable::printStackTrace);
    }

}

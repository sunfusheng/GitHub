package com.sunfusheng.github.ui.repo;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.ui.base.BaseFragment;
import com.sunfusheng.github.util.Utils;
import com.sunfusheng.github.viewmodel.ReadmeViewModel;
import com.sunfusheng.github.viewmodel.vm.VMProviders;
import com.sunfusheng.github.widget.app.ReadMeWebView;
import com.sunfusheng.multistate.LoadingState;
import com.sunfusheng.multistate.MultiStateView;

/**
 * @author by sunfusheng on 2018/11/14
 */
public class RepoDetailFragment extends BaseFragment {

    private MultiStateView vMultiStateView;
    private ReadMeWebView vReadMe;

    private String repoName;
    private String username;
    private String repoFullName;
    private String mReadme;

    public static RepoDetailFragment instance(String repoFullName) {
        RepoDetailFragment fragment = new RepoDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Bundle.REPO_FULL_NAME, repoFullName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle arguments) {
        if (arguments != null) {
            repoFullName = arguments.getString(Constants.Bundle.REPO_FULL_NAME);
        }
        username = Utils.getUserName(repoFullName);
        repoName = Utils.getRepoName(repoFullName);

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(repoName)) {
            throw new IllegalArgumentException("please set repoFullName");
        }
    }

    @Override
    public int inflateLayout() {
        return R.layout.fragment_repo_detail;
    }

    @Override
    public void initView(@NonNull View rootView) {
        vMultiStateView = rootView.findViewById(R.id.multiStateView);
        vReadMe = rootView.findViewById(R.id.vReadMe);

        initReadmeView();
    }

    @Override
    protected void initToolBar() {
        super.initToolBar();
        toolbar.setTitle(repoName);
    }

    private void initReadmeView() {
        vReadMe.setVisibility(View.GONE);
        vMultiStateView.setVisibility(View.VISIBLE);
        vMultiStateView.setLoadingState(LoadingState.LOADING);

        ReadmeViewModel viewModel = VMProviders.of(this, ReadmeViewModel.class);
        viewModel.liveData.observe(this, it -> {
            Log.d("sfs", "fetchReadme() loadingState: " + it.loadingStateString + " fetchMode: " + it.fetchModeString);

            if (it.loadingState == LoadingState.SUCCESS) {
                vMultiStateView.setVisibility(View.GONE);
                vReadMe.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(mReadme) || mReadme.hashCode() != it.data.hashCode()) {
                    mReadme = it.data;
                    vReadMe.setReadme(repoFullName, mReadme);
                }
            }
        });

        viewModel.request(repoFullName, FetchMode.REMOTE);
    }
}

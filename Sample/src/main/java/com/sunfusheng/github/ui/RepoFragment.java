package com.sunfusheng.github.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.viewbinder.RepoBinder;
import com.sunfusheng.github.viewmodel.RepoViewModel;
import com.sunfusheng.github.viewmodel.base.VmProvider;
import com.sunfusheng.wrapper.RecyclerViewWrapper;

/**
 * @author sunfusheng on 2018/7/25.
 */
public class RepoFragment extends BaseFragment {

    private RecyclerViewWrapper recyclerViewWrapper;
    private String username;

    public static RepoFragment newFragment(String username) {
        RepoFragment fragment = new RepoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Bundle.USERNAME, username);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(Constants.Bundle.USERNAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_recyclerview_wrapper, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewWrapper = view.findViewById(R.id.recyclerViewWrapper);
        recyclerViewWrapper.enableRefresh(false);
        recyclerViewWrapper.enableLoadMore(false);

//        recyclerViewWrapper.getRefreshLayout().

        recyclerViewWrapper.register(Repo.class, new RepoBinder());

        observeRepos();
    }

    private void observeRepos() {
        RepoViewModel viewModel = VmProvider.of(this, RepoViewModel.class);
        viewModel.setRequestParams(username, 1, Constants.PER_PAGE_30, FetchMode.DEFAULT);

        viewModel.liveData.observe(this, it -> {
            recyclerViewWrapper.setItems(it.data);
        });
    }

}

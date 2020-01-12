package com.sunfusheng.github.ui.repo;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.ui.NavigationManager;
import com.sunfusheng.github.ui.base.BaseListFragment;
import com.sunfusheng.github.viewbinder.RepoBinder;
import com.sunfusheng.github.viewmodel.UserOwnedRepoListViewModel;

/**
 * @author sunfusheng
 * @since 2020-01-07
 */
public class UserOwnedRepoListFragment extends BaseListFragment<UserOwnedRepoListViewModel, Repo> {

    public static UserOwnedRepoListFragment newFragment(String username) {
        UserOwnedRepoListFragment fragment = new UserOwnedRepoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Bundle.USERNAME, username);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Class<UserOwnedRepoListViewModel> getViewModelClass() {
        return UserOwnedRepoListViewModel.class;
    }

    @Override
    protected void registerViewBinders() {
        RepoBinder repoBinder = new RepoBinder();
        repoBinder.showFullName(false);
        repoBinder.showExactNum(true);
        repoBinder.showUpdateTime(true);
        vRecyclerViewWrapper.register(Repo.class, repoBinder);
    }

    @Override
    protected void onItemClick(Repo item) {
        NavigationManager.toRepoDetailActivity(getContext(), item.full_name);
    }
}

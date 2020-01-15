package com.sunfusheng.github.ui.user;

import android.os.Bundle;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.ui.NavigationManager;
import com.sunfusheng.github.ui.base.BaseListFragment;
import com.sunfusheng.github.viewbinder.RepoBinder;
import com.sunfusheng.github.viewmodel.UserStarredRepoListViewModel;

/**
 * @author sunfusheng
 * @since 2020-01-12
 */
public class StarredRepoListFragment extends BaseListFragment<UserStarredRepoListViewModel, Repo> {

    public static StarredRepoListFragment newFragment(String username) {
        StarredRepoListFragment fragment = new StarredRepoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Bundle.USERNAME, username);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected Class<UserStarredRepoListViewModel> getViewModelClass() {
        return UserStarredRepoListViewModel.class;
    }

    @Override
    protected void registerViewBinders() {
        RepoBinder repoBinder = new RepoBinder();
        repoBinder.showFullName(true);
        repoBinder.showExactNum(false);
        repoBinder.showUpdateTime(true);
        vRecyclerViewWrapper.register(Repo.class, repoBinder);
    }

    @Override
    protected void onItemClick(Repo item) {
        NavigationManager.toRepoDetailActivity(getContext(), item.full_name);
    }
}

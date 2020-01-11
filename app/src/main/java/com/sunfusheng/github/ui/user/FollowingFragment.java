package com.sunfusheng.github.ui.user;

import android.os.Bundle;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.ui.NavigationManager;
import com.sunfusheng.github.ui.base.BaseListFragment;
import com.sunfusheng.github.viewbinder.UserListItemBinder;
import com.sunfusheng.github.viewmodel.FollowingViewModel;

/**
 * @author sunfusheng
 * @since 2020-01-11
 */
public class FollowingFragment extends BaseListFragment<FollowingViewModel, User> {

    public static FollowingFragment newFragment(String username) {
        FollowingFragment fragment = new FollowingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Bundle.USERNAME, username);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected Class<FollowingViewModel> getViewModelClass() {
        return FollowingViewModel.class;
    }

    @Override
    protected void registerViewBinders() {
        vRecyclerViewWrapper.register(User.class, new UserListItemBinder());
    }

    @Override
    protected void onItemClick(User item) {
        NavigationManager.toUserActivity(getContext(), item);
    }
}

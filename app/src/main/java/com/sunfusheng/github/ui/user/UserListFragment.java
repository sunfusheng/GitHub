package com.sunfusheng.github.ui.user;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.ui.NavigationManager;
import com.sunfusheng.github.ui.base.BaseListFragment;
import com.sunfusheng.github.viewbinder.UserListItemBinder;
import com.sunfusheng.github.viewmodel.UserListViewModel;

/**
 * @author sunfusheng
 * @since 2020-01-11
 */
public class UserListFragment extends BaseListFragment<UserListViewModel, User> {

    private String mUserListFrom;

    public static UserListFragment newFragment(String username, String userListFrom) {
        UserListFragment fragment = new UserListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Bundle.USERNAME, username);
        bundle.putString(Constants.Bundle.USER_LIST_FROM, userListFrom);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData(@Nullable Bundle arguments) {
        super.initData(arguments);
        if (arguments != null) {
            mUserListFrom = arguments.getString(Constants.Bundle.USER_LIST_FROM);
        }
        mVM.userListFrom = mUserListFrom;

        Log.d("sfs", "UserListFragment: " + mUserListFrom);
    }

    @Override
    protected Class<UserListViewModel> getViewModelClass() {
        return UserListViewModel.class;
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

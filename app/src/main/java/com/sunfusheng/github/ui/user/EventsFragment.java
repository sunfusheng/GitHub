package com.sunfusheng.github.ui.user;

import android.os.Bundle;
import android.text.TextUtils;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.ui.NavigationManager;
import com.sunfusheng.github.ui.base.BaseListFragment;
import com.sunfusheng.github.viewbinder.UserEventBinder;
import com.sunfusheng.github.viewmodel.UserEventsViewModel;

/**
 * @author sunfusheng
 * @since 2020-01-15
 */
public class EventsFragment extends BaseListFragment<UserEventsViewModel, Event> {

    public static EventsFragment newFragment(String username) {
        EventsFragment fragment = new EventsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Bundle.USERNAME, username);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected Class<UserEventsViewModel> getViewModelClass() {
        return UserEventsViewModel.class;
    }

    @Override
    protected void registerViewBinders() {
        vRecyclerViewWrapper.register(Event.class, new UserEventBinder());
    }

    @Override
    protected void onItemClick(Event item) {
        if (item.repo != null && !TextUtils.isEmpty(item.repo.name)) {
            NavigationManager.toRepoDetailActivity(getContext(), item.repo.name);
        }
    }
}
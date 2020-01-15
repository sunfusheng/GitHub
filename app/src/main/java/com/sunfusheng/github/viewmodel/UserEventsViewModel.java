package com.sunfusheng.github.viewmodel;

import com.sunfusheng.github.datasource.BaseDataSource;
import com.sunfusheng.github.datasource.UserEventsDataSource;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.viewmodel.params.UsernamePageParams;

import java.util.List;

/**
 * @author sunfusheng
 * @since 2020-01-15
 */
public class UserEventsViewModel extends BaseListViewModel<UsernamePageParams, Event> {
    @Override
    UsernamePageParams getPageParams() {
        return new UsernamePageParams(username);
    }

    @Override
    BaseDataSource<UsernamePageParams, List<Event>> getDataSource() {
        return new UserEventsDataSource();
    }
}

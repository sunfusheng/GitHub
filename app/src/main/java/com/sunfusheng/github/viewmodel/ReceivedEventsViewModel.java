package com.sunfusheng.github.viewmodel;

import com.sunfusheng.github.datasource.BaseDataSource;
import com.sunfusheng.github.datasource.ReceivedEventsDataSource;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.viewmodel.params.UsernamePageParams;

import java.util.List;

/**
 * @author sunfusheng on 2018/5/7.
 */
public class ReceivedEventsViewModel extends BaseListViewModel<UsernamePageParams, Event> {
    @Override
    UsernamePageParams getPageParams() {
        return new UsernamePageParams(username);
    }

    @Override
    BaseDataSource<UsernamePageParams, List<Event>> getDataSource() {
        return new ReceivedEventsDataSource();
    }
}

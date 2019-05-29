package com.sunfusheng.github.viewmodel;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.ReceivedEventsDataSource;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.viewmodel.params.UsernamePageParams;

import java.util.List;

/**
 * @author sunfusheng on 2018/5/7.
 */
public class ReceivedEventsViewModel extends BaseViewModel<UsernamePageParams, List<Event>> {

    public void request(String username, int page, int pageCount, @FetchMode int fetchMode) {
        request(new UsernamePageParams(username, page, pageCount, fetchMode),
                new ReceivedEventsDataSource(username, page, pageCount, fetchMode));
    }
}

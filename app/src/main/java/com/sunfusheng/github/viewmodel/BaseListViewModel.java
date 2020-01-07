package com.sunfusheng.github.viewmodel;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.BaseDataSource;
import com.sunfusheng.github.viewmodel.params.PageParams;

/**
 * @author sunfusheng
 * @since 2020-01-06
 */
abstract public class BaseListViewModel<P extends PageParams, R> extends BaseViewModel<P, R> {
    public static final int MODE_LOAD = 0;
    public static final int MODE_REFRESH = 1;
    public static final int MODE_LOAD_MORE = 2;

    private int mLoadMode = MODE_LOAD;
    private int mPage = PageParams.FIRST_PAGE;

    public final int getLoadMode() {
        return mLoadMode;
    }

    public final boolean isLoadMode() {
        return mLoadMode == MODE_LOAD;
    }

    public final boolean isRefreshMode() {
        return mLoadMode == MODE_REFRESH;
    }

    public final boolean isLoadMoreMode() {
        return mLoadMode == MODE_LOAD_MORE;
    }

    public final int getPage() {
        return mPage;
    }

    public final boolean isFirstPage() {
        return mPage == PageParams.FIRST_PAGE;
    }

    public int getPageCount() {
        return PageParams.PAGE_COUNT;
    }

    abstract P getPageParams();

    abstract BaseDataSource<P, R> getDataSource();

    protected void doRequest(@FetchMode int fetchMode) {
        P params = getPageParams();
        params.page = getPage();
        params.pageCount = getPageCount();
        params.fetchMode = fetchMode;
        BaseDataSource<P, R> dataSource = getDataSource();
        dataSource.setParams(params);
        request(params, dataSource);
    }

    // 加载
    public void load() {
        mLoadMode = MODE_LOAD;
        mPage = PageParams.FIRST_PAGE;
        doRequest(FetchMode.REMOTE);
    }

    // 下拉刷新
    public void refresh() {
        mLoadMode = MODE_REFRESH;
        mPage = PageParams.FIRST_PAGE;
        doRequest(FetchMode.FORCE_REMOTE);
    }

    // 加载下一页
    public void loadMore() {
        mLoadMode = MODE_LOAD_MORE;
        mPage++;
        doRequest(FetchMode.FORCE_REMOTE);
    }

    @Override
    protected void loadError(int fetchMode, int code, String msg) {
        if (fetchMode == FetchMode.REMOTE || fetchMode == FetchMode.FORCE_REMOTE) {
            onErrorOrEmpty();
        }
    }

    @Override
    protected void loadEmpty(int fetchMode) {
        if (fetchMode == FetchMode.REMOTE || fetchMode == FetchMode.FORCE_REMOTE) {
            onErrorOrEmpty();
        }
    }

    private void onErrorOrEmpty() {
        if (mPage > PageParams.FIRST_PAGE) {
            mPage--;
        }
    }
}

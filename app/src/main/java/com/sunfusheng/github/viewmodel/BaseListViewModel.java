package com.sunfusheng.github.viewmodel;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.BaseDataSource;
import com.sunfusheng.github.viewmodel.params.PageParams;

import java.util.List;

/**
 * @author sunfusheng
 * @since 2020-01-06
 */
abstract public class BaseListViewModel<P extends PageParams, R> extends BaseViewModel<P, List<R>> {
    private static final int MODE_LOAD = 0;
    private static final int MODE_REFRESH = 1;
    private static final int MODE_LOAD_MORE = 2;

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

    abstract BaseDataSource<P, List<R>> getDataSource();

    private P mParams;
    private BaseDataSource<P, List<R>> mDataSource;

    private void doRequest(@FetchMode int fetchMode) {
        if (mParams == null) {
            mParams = getPageParams();
        }
        mParams.page = getPage();
        mParams.pageCount = getPageCount();
        mParams.fetchMode = fetchMode;

        if (mDataSource == null) {
            mDataSource = getDataSource();
        }
        mDataSource.setParams(mParams);
        request(mParams, mDataSource);
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
        onErrorOrEmpty(fetchMode);
    }

    @Override
    protected void loadEmpty(int fetchMode) {
        onErrorOrEmpty(fetchMode);
    }

    private void onErrorOrEmpty(@FetchMode int fetchMode) {
        if (fetchMode == FetchMode.REMOTE || fetchMode == FetchMode.FORCE_REMOTE) {
            if (mPage > PageParams.FIRST_PAGE) {
                mPage--;
            }
        }
    }
}

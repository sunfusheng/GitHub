package com.sunfusheng.github.viewmodel.params;

import android.support.annotation.NonNull;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.http.response.ResponseData;

/**
 * @author by sunfusheng on 2019-05-28
 */
public class PageParams extends BaseParams {
    public static final int FIRST_PAGE = 1;
    public static final int PAGE_COUNT = 30;

    public int page;
    public int pageCount;

    public PageParams(int page, @FetchMode int fetchMode) {
        this(page, PAGE_COUNT, fetchMode);
    }

    public PageParams(int page, int pageCount, @FetchMode int fetchMode) {
        super(fetchMode);
        this.page = page;
        this.pageCount = pageCount;
    }

    @NonNull
    @Override
    public String toString() {
        return "PageParams{" +
                "page=" + page +
                ", pageCount=" + pageCount +
                ", fetchMode=" + ResponseData.getFetchModeString(fetchMode) +
                '}';
    }
}

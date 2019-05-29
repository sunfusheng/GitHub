package com.sunfusheng.github.viewmodel.params;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.net.response.ResponseData;

/**
 * @author by sunfusheng on 2019-05-28
 */
public class PageParams extends BaseParams {
    public int page;
    public int pageCount;

    public PageParams(int page, int pageCount, @FetchMode int fetchMode) {
        super(fetchMode);
        this.page = page;
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        return "PageParams{" +
                "page=" + page +
                ", pageCount=" + pageCount +
                ", fetchMode=" + ResponseData.getFetchModeString(fetchMode) +
                '}';
    }
}

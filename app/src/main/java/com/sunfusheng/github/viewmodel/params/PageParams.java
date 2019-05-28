package com.sunfusheng.github.viewmodel.params;

import com.sunfusheng.github.annotation.FetchMode;

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
}

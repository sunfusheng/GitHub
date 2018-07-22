package com.sunfusheng.github.widget.span;

/**
 * @author sunfusheng on 2018/7/22.
 */
public interface ISpanTouch {
    /**
     * 记录当前 Touch 事件对应的点是不是点在了 span 上面
     */
    void setTouchSpanHit(boolean hit);
}

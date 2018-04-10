package com.sunfusheng.github.widget.RadiusWidget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.sunfusheng.github.R;
import com.sunfusheng.github.util.ViewUtil;


/**
 * @author sunfusheng on 2018/1/20.
 */
public class RadiusFrameLayout extends FrameLayout {

    public RadiusFrameLayout(Context context) {
        this(context, null);
    }

    public RadiusFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.DefaultAttr);
    }

    public RadiusFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        RadiusGradientDrawable drawable = RadiusGradientDrawable.fromAttributeSet(context, attrs, defStyleAttr);
        ViewUtil.setBackgroundKeepingPadding(this, drawable);
    }

}

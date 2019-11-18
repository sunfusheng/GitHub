package com.sunfusheng.github.widget.radiusview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.sunfusheng.github.R;
import com.sunfusheng.github.util.ViewUtil;


/**
 * @author sunfusheng on 2018/1/20.
 */
public class RadiusLinearLayout extends LinearLayout {

    public RadiusLinearLayout(Context context) {
        this(context, null);
    }

    public RadiusLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.DefaultAttr);
    }

    public RadiusLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        RadiusGradientDrawable drawable = RadiusGradientDrawable.fromAttributeSet(context, attrs, defStyleAttr);
        ViewUtil.setBackgroundKeepingPadding(this, drawable);
    }
}

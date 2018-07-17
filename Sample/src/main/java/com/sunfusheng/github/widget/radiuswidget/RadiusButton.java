package com.sunfusheng.github.widget.radiuswidget;

import android.content.Context;
import android.util.AttributeSet;

import com.sunfusheng.github.R;
import com.sunfusheng.github.util.ViewUtil;

/**
 * @author sunfusheng on 2018/1/20.
 */
public class RadiusButton extends android.support.v7.widget.AppCompatButton {

    public RadiusButton(Context context) {
        this(context, null);
    }

    public RadiusButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.DefaultAttr);
    }

    public RadiusButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        RadiusGradientDrawable drawable = RadiusGradientDrawable.fromAttributeSet(context, attrs, defStyleAttr);
        ViewUtil.setBackgroundKeepingPadding(this, drawable);
    }

}

package com.sunfusheng.github.widget.span;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * @author sunfusheng on 2018/7/22.
 */
public class SpanTouchTextView extends AppCompatTextView implements ISpanTouch {
    /**
     * 记录当前 Touch 事件对应的点是不是点在了 span 上面
     */
    private boolean mTouchSpanHit;

    /**
     * 记录每次真正传入的press，每次更改mTouchSpanHint，需要再调用一次setPressed，确保press状态正确
     */
    private boolean mIsPressedRecord = false;
    /**
     * TextView是否应该消耗事件
     */
    private boolean mNeedForceEventToParent = false;

    public SpanTouchTextView(Context context) {
        this(context, null);
    }

    public SpanTouchTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpanTouchTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setHighlightColor(Color.TRANSPARENT);
    }

    public void setNeedForceEventToParent(boolean needForceEventToParent) {
        mNeedForceEventToParent = needForceEventToParent;
        setFocusable(!needForceEventToParent);
        setClickable(!needForceEventToParent);
        setLongClickable(!needForceEventToParent);
    }

    /**
     * 使用者主动调用
     */
    public void setMovementMethodDefault() {
        setMovementMethodCompat(LinkTouchMovementMethod.getInstance());
    }

    public void setMovementMethodCompat(MovementMethod movement) {
        setMovementMethod(movement);
        if (mNeedForceEventToParent) {
            setNeedForceEventToParent(true);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!(getText() instanceof Spannable)) {
            return super.onTouchEvent(event);
        }
        mTouchSpanHit = true;
        // 调用super.onTouchEvent,会走到QMUILinkTouchMovementMethod
        // 会走到QMUILinkTouchMovementMethod#onTouchEvent会修改mTouchSpanHint
        boolean ret = super.onTouchEvent(event);
        if (mNeedForceEventToParent) {
            return mTouchSpanHit;
        }
        return ret;
    }

    @Override
    public void setTouchSpanHit(boolean hit) {
        if (mTouchSpanHit != hit) {
            mTouchSpanHit = hit;
            setPressed(mIsPressedRecord);
        }
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean performClick() {
        if (!mTouchSpanHit && !mNeedForceEventToParent) {
            return super.performClick();
        }
        return false;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean performLongClick() {
        if (!mTouchSpanHit && !mNeedForceEventToParent) {
            return super.performLongClick();
        }
        return false;
    }

    @Override
    public final void setPressed(boolean pressed) {
        mIsPressedRecord = pressed;
        if (!mTouchSpanHit) {
            onSetPressed(pressed);
        }
    }

    protected void onSetPressed(boolean pressed) {
        super.setPressed(pressed);
    }
}

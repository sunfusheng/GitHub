package com.sunfusheng.github.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.sunfusheng.github.R;

/**
 * @author sunfusheng on 2018/7/15.
 */
public class CircleTextView extends CircleView {
    private String text = "";
    private int textColor = 0x999999;
    private int textSize = 15;

    private Paint textPaint;

    public CircleTextView(Context context) {
        this(context, null);
    }

    public CircleTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleTextView, defStyleAttr, 0);
        text = typedArray.getString(R.styleable.CircleTextView_viewText);
        textSize = (int) typedArray.getDimension(R.styleable.CircleTextView_viewTextSize, textSize);
        textColor = typedArray.getColor(R.styleable.CircleTextView_viewTextColor, textColor);
        typedArray.recycle();

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float baseline = (getMeasuredHeight() - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText(text, getMeasuredHeight() / 2, baseline, textPaint);
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        textPaint.setTextSize(textSize);
        invalidate();
    }

    public void setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
        textPaint.setColor(textColor);
        invalidate();
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }
}

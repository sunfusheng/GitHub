package com.sunfusheng.github.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.sunfusheng.github.R;

/**
 * @author sunfusheng on 2018/7/15.
 */
public class CircleView extends View {
    private int circleColor = 0x999999;
    private int circleRadius = 50;
    private int circleBorder = 0;
    private int circleBorderColor = 0x333333;

    private Paint circlePaint;
    private Paint borderPaint;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView, defStyleAttr, 0);
        circleColor = typedArray.getColor(R.styleable.CircleView_circleColor, circleColor);
        circleRadius = (int) typedArray.getDimension(R.styleable.CircleView_circleRadius, circleRadius);
        circleBorder = (int) typedArray.getDimension(R.styleable.CircleView_circleBorder, circleBorder);
        circleBorderColor = typedArray.getColor(R.styleable.CircleView_circleBorderColor, circleBorderColor);
        typedArray.recycle();

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(circleColor);
        circlePaint.setStrokeWidth(0);

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(circleBorder);
        borderPaint.setColor(circleBorderColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((circleRadius + circleBorder) * 2, (circleRadius + circleBorder) * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(circleRadius + circleBorder, circleRadius + circleBorder, circleRadius, circlePaint);
        canvas.drawCircle(circleRadius + circleBorder, circleRadius + circleBorder, circleRadius, borderPaint);
    }

    public void setCircleBorder(int circleBorder) {
        this.circleBorder = circleBorder;
        borderPaint.setStrokeWidth(circleBorder);
        invalidate();
    }

    public void setCircleBorderColor(@ColorInt int circleBorderColor) {
        this.circleBorderColor = circleBorderColor;
        borderPaint.setColor(circleBorderColor);
        invalidate();
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
        invalidate();
    }

    public void setCircleColor(@ColorInt int circleColor) {
        this.circleColor = circleColor;
        circlePaint.setColor(circleColor);
        invalidate();
    }
}

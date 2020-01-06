package com.sunfusheng.github.widget.titlebar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.sunfusheng.github.R;

/**
 * @author sunfusheng on 2018/4/19.
 */
public class CustomTitleBar extends RelativeLayout {

    private LinearLayout vLeftArea;
    private ImageView vLeftImg;
    private TextView vLeftText;
    private TextView vTitle;
    private LinearLayout vRightArea;
    private ImageView vRightImg;
    private ImageView vRightImg2;
    private TextView vRightText;

    public CustomTitleBar(Context context) {
        this(context, null);
    }

    public CustomTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(inflateResource(), this);
        vLeftArea = findViewById(R.id.left_area);
        vLeftImg = findViewById(R.id.left_img);
        vLeftText = findViewById(R.id.left_text);
        vTitle = findViewById(R.id.title);
        vRightArea = findViewById(R.id.right_area);
        vRightImg = findViewById(R.id.right_img);
        vRightImg2 = findViewById(R.id.right_img2);
        vRightText = findViewById(R.id.right_text);
    }

    public int inflateResource() {
        return R.layout.layout_title_bar;
    }

    public void setTitle(@StringRes int title) {
        setTitle(getResources().getString(title));
    }

    public void setTitle(CharSequence title) {
        vTitle.setVisibility(VISIBLE);
        vTitle.setText(title);
    }

    public void setTitleClickListener(OnClickListener listener) {
        vTitle.setVisibility(VISIBLE);
        vTitle.setOnClickListener(listener);
    }

    public void setLeftImg(@DrawableRes int resId) {
        vLeftImg.setVisibility(VISIBLE);
        vLeftImg.setImageResource(resId);
    }

    public void setLeftImgClickListener(OnClickListener listener) {
        vLeftImg.setVisibility(VISIBLE);
        vLeftImg.setOnClickListener(listener);
    }

    public void setLeftText(@StringRes int text) {
        setLeftText(getResources().getString(text));
    }

    public void setLeftText(CharSequence text) {
        vLeftText.setVisibility(VISIBLE);
        vLeftText.setText(text);
    }

    public void setLeftTextClickListener(OnClickListener listener) {
        vLeftText.setVisibility(VISIBLE);
        vLeftText.setOnClickListener(listener);
    }

    public void setRightImg(@DrawableRes int resId) {
        vRightImg.setVisibility(VISIBLE);
        vRightImg.setImageResource(resId);
    }

    public void setRightImgClickListener(OnClickListener listener) {
        vRightImg.setVisibility(VISIBLE);
        vRightImg.setOnClickListener(listener);
    }

    public void setRightImg2(@DrawableRes int resId) {
        vRightImg2.setVisibility(VISIBLE);
        vRightImg2.setImageResource(resId);
    }

    public void setRightImg2ClickListener(OnClickListener listener) {
        vRightImg2.setVisibility(VISIBLE);
        vRightImg2.setOnClickListener(listener);
    }

    public void setRightText(@StringRes int text) {
        setTitle(getResources().getString(text));
    }

    public void setRightText(CharSequence text) {
        vRightText.setVisibility(VISIBLE);
        vRightText.setText(text);
    }

    public void setRightTextClickListener(OnClickListener listener) {
        vRightText.setVisibility(VISIBLE);
        vRightText.setOnClickListener(listener);
    }

    public LinearLayout getLeftArea() {
        return vLeftArea;
    }

    public ImageView getLeftImg() {
        return vLeftImg;
    }

    public TextView getLeftText() {
        return vLeftText;
    }

    public TextView getTitle() {
        return vTitle;
    }

    public LinearLayout getRightArea() {
        return vRightArea;
    }

    public ImageView getRightImg() {
        return vRightImg;
    }

    public ImageView getRightImg2() {
        return vRightImg2;
    }

    public TextView getRightText() {
        return vRightText;
    }
}

package com.sunfusheng.github.widget.bottombar;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class AlphaTabLayout extends LinearLayout {

    private ViewPager mViewPager;
    private OnTabChangedListener mListener;
    private List<AlphaTabView> mTabViews;
    private boolean ISINIT;
    /**
     * 子View的数量
     */
    private int mChildCounts;
    /**
     * 当前的条目索引
     */
    private int mCurrentItem = 0;

    public AlphaTabLayout(Context context) {
        this(context, null);
    }

    public AlphaTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlphaTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        post(this::isInit);
    }

    public void setViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
        init();
    }

    public void setOnTabChangedListener(OnTabChangedListener listener) {
        this.mListener = listener;
        isInit();
    }

    public AlphaTabView getCurrentItemView() {
        isInit();
        return mTabViews.get(mCurrentItem);
    }

    public AlphaTabView getTabView(int tabIndex) {
        isInit();
        return mTabViews.get(tabIndex);
    }

    public void removeAllBadge() {
        isInit();
        for (AlphaTabView alphaTabView : mTabViews) {
            alphaTabView.removeShow();
        }
    }

    public void setTabCurrenItem(int tabIndex) {
        if (tabIndex < mChildCounts && tabIndex > -1) {
            mTabViews.get(tabIndex).performClick();
        } else {
            throw new IllegalArgumentException("IndexOutOfBoundsException");
        }

    }

    private void isInit() {
        if (!ISINIT) {
            init();
        }
    }

    private void init() {
        ISINIT = true;
        mTabViews = new ArrayList<>();
        mChildCounts = getChildCount();

        if (null != mViewPager) {
            if (null == mViewPager.getAdapter()) {
                throw new NullPointerException("viewpager的adapter为null");
            }
            if (mViewPager.getAdapter().getCount() != mChildCounts) {
                throw new IllegalArgumentException("子View数量必须和ViewPager条目数量一致");
            }
            //对ViewPager添加监听
            mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        }

        for (int i = 0; i < mChildCounts; i++) {
            if (getChildAt(i) instanceof AlphaTabView) {
                AlphaTabView tabView = (AlphaTabView) getChildAt(i);
                mTabViews.add(tabView);
                //设置点击监听
                tabView.setOnClickListener(new MyOnClickListener(i));
            } else {
                throw new IllegalArgumentException("TabIndicator的子View必须是TabView");
            }
        }

        mTabViews.get(mCurrentItem).setIconAlpha(1.0f);
    }

    private class MyOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //滑动时的透明度动画
            if (positionOffset > 0) {
                mTabViews.get(position).setIconAlpha(1 - positionOffset);
                mTabViews.get(position + 1).setIconAlpha(positionOffset);
            }
            //滑动时保存当前按钮索引
            mCurrentItem = position;
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            resetState();
            mTabViews.get(position).setIconAlpha(1.0f);
            mCurrentItem = position;
        }
    }

    private class MyOnClickListener implements OnClickListener {

        private int currentIndex;

        public MyOnClickListener(int i) {
            this.currentIndex = i;
        }

        @Override
        public void onClick(View v) {
            //点击前先重置所有按钮的状态
            resetState();
            mTabViews.get(currentIndex).setIconAlpha(1.0f);
            if (null != mListener) {
                mListener.onTabChanged(currentIndex);
            }
            if (null != mViewPager) {
                //不能使用平滑滚动，否者颜色改变会乱
                mViewPager.setCurrentItem(currentIndex, false);
            }
            //点击是保存当前按钮索引
            mCurrentItem = currentIndex;
        }
    }

    /**
     * 重置所有按钮的状态
     */
    private void resetState() {
        for (int i = 0; i < mChildCounts; i++) {
            mTabViews.get(i).setIconAlpha(0);
        }
    }

    private static final String STATE_INSTANCE = "instance_state";
    private static final String STATE_ITEM = "state_item";

    /**
     * @return 当View被销毁的时候，保存数据
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_ITEM, mCurrentItem);
        return bundle;
    }

    /**
     * @param state 用于恢复数据使用
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentItem = bundle.getInt(STATE_ITEM);
            if (null == mTabViews || mTabViews.isEmpty()) {
                super.onRestoreInstanceState(bundle.getParcelable(STATE_INSTANCE));
                return;
            }
            //重置所有按钮状态
            resetState();
            //恢复点击的条目颜色
            mTabViews.get(mCurrentItem).setIconAlpha(1.0f);
            super.onRestoreInstanceState(bundle.getParcelable(STATE_INSTANCE));
        } else {
            super.onRestoreInstanceState(state);
        }
    }
}

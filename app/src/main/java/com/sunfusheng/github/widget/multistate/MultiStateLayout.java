package com.sunfusheng.github.widget.multistate;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.LoadingState;

/**
 * @author sunfusheng on 2018/4/19.
 */
public class MultiStateLayout extends FrameLayout {

    private View loadingView;
    private View errorView;
    private View emptyView;

    @LoadingState
    private int state;
    private LoadingStateDelegate delegate;

    public MultiStateLayout(@NonNull Context context) {
        this(context, null);
    }

    public MultiStateLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStateLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MultiStateLayout, defStyleAttr, 0);
        loadingView = inflater.inflate(array.getResourceId(R.styleable.MultiStateLayout_loadingLayout, R.layout.layout_loading), this, false);
        errorView = inflater.inflate(array.getResourceId(R.styleable.MultiStateLayout_errorLayout, R.layout.layout_error), this, false);
        emptyView = inflater.inflate(array.getResourceId(R.styleable.MultiStateLayout_emptyLayout, R.layout.layout_empty), this, false);
        array.recycle();

        addView(loadingView);
        addView(errorView);
        addView(emptyView);
        delegate = new LoadingStateDelegate(loadingView, errorView, emptyView);
        setLoadingState(LoadingState.SUCCESS);
    }

    public void setLoadingState(@LoadingState int state) {
        this.state = state;
        delegate.setLoadingState(state);
    }

    public void setLoadingView(View loadingView) {
        delegate.setLoadingView(loadingView);
    }

    public void setErrorView(View errorView) {
        delegate.setErrorView(errorView);
    }

    public void setEmptyView(View emptyView) {
        delegate.setEmptyView(emptyView);
    }

    public void setErrorTip(String tip) {
        if (TextUtils.isEmpty(tip)) {
            return;
        }
        TextView textView = errorView.findViewById(R.id.error_tip);
        if (textView != null) {
            textView.setText(tip);
        }
    }

    public void setErrorViewListener(OnClickListener listener) {
        errorView.setOnClickListener(listener);
    }

    public void setErrorButtonListener(OnClickListener listener) {
        View view = errorView.findViewById(R.id.error_button);
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }

    public void setEmptyViewListener(OnClickListener listener) {
        emptyView.setOnClickListener(listener);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.state = state;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        if (ss != null) {
            setLoadingState(ss.state);
            super.onRestoreInstanceState(ss.getSuperState());
        }
    }

    static class SavedState extends BaseSavedState {
        int state;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.state = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.state);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}

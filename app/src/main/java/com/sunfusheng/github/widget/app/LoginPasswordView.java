package com.sunfusheng.github.widget.app;

import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.sunfusheng.github.R;

/**
 * @author sunfusheng
 * @since 2020-01-10
 */
public class LoginPasswordView extends ConstraintLayout {

    private ImageView vPrefixIcon;
    private EditText vContent;
    private ImageView vSuffixIcon;

    private boolean showPassword;
    private Runnable mCommittedCallback;

    public LoginPasswordView(Context context) {
        this(context, null);
    }

    public LoginPasswordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoginPasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_login_edittext, this);
        vPrefixIcon = findViewById(R.id.vPrefixIcon);
        vContent = findViewById(R.id.vContent);
        vSuffixIcon = findViewById(R.id.vSuffixIcon);

        vPrefixIcon.setBackgroundResource(R.drawable.ic_lock_black);
        vContent.setHint(R.string.password_input);
        vContent.setTransformationMethod(PasswordTransformationMethod.getInstance());
        vSuffixIcon.setBackgroundResource(R.drawable.ic_visibility_off_black);

        vContent.setOnFocusChangeListener((v, hasFocus) -> {
            vPrefixIcon.setBackgroundResource(hasFocus ? R.drawable.ic_lock_red : R.drawable.ic_lock_black);
        });

        vContent.setImeOptions(EditorInfo.IME_ACTION_SEND);
        vContent.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                if (mCommittedCallback != null) {
                    mCommittedCallback.run();
                    return true;
                }
            }
            return false;
        });

        vSuffixIcon.setOnClickListener(v -> {
            showPassword = !showPassword;
            if (showPassword) {
                vSuffixIcon.setBackgroundResource(R.drawable.ic_visibility_black);
                vContent.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                vSuffixIcon.setBackgroundResource(R.drawable.ic_visibility_off_black);
                vContent.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            vContent.setSelection(getPassword().length());
        });
    }

    public EditText getEditText() {
        return vContent;
    }

    public void setCommittedCallback(Runnable callback) {
        this.mCommittedCallback = callback;
    }

    public void clearEditTextFocus() {
        if (vContent.hasFocus()) {
            vContent.clearFocus();
        }
    }

    public String getPassword() {
        return vContent.getText().toString();
    }
}

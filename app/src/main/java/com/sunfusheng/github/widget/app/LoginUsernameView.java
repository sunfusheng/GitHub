package com.sunfusheng.github.widget.app;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.sunfusheng.github.R;

/**
 * @author sunfusheng
 * @since 2020-01-10
 */
public class LoginUsernameView extends ConstraintLayout {

    private ImageView vPrefixIcon;
    private EditText vContent;
    private ImageView vSuffixIcon;

    public LoginUsernameView(Context context) {
        this(context, null);
    }

    public LoginUsernameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoginUsernameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_login_edittext, this);
        vPrefixIcon = findViewById(R.id.vPrefixIcon);
        vContent = findViewById(R.id.vContent);
        vSuffixIcon = findViewById(R.id.vSuffixIcon);

        vPrefixIcon.setBackgroundResource(R.drawable.ic_person_black);
        vContent.setHint(R.string.username_input);
        vSuffixIcon.setVisibility(INVISIBLE);
        vSuffixIcon.setBackgroundResource(R.drawable.ic_clear_black);

        vContent.setOnFocusChangeListener((v, hasFocus) -> {
            vPrefixIcon.setBackgroundResource(hasFocus ? R.drawable.ic_person_red : R.drawable.ic_person_black);
        });

        vContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vSuffixIcon.setVisibility(TextUtils.isEmpty(s) ? INVISIBLE : VISIBLE);
            }
        });

        vSuffixIcon.setOnClickListener(v -> {
            vContent.setText("");
        });
    }

    public EditText getEditText() {
        return vContent;
    }

    public void clearEditTextFocus() {
        if (vContent.hasFocus()) {
            vContent.clearFocus();
        }
    }

    public String getUsername() {
        return vContent.getText().toString();
    }
}

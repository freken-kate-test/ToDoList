package ru.test.todolist.customViews;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.test.todolist.utils.CommonUtils;
import ru.test.todolist.R;

public class TDInputField extends TDLinearLayout {

    //region Views
    @BindView(R.id.input_et)
    protected EditText mEtInput;
    @BindView(R.id.password_eye_iv)
    protected CheckableImageView mIvPasswordToggle;
    @BindView(R.id.tv_error)
    protected TextView mTvError;
    @BindView(R.id.edit_container)
    protected ViewGroup mEditContainer;
    private View viewStub;
    //endregion

    private Drawable mPasswordToggleDrawable;
    private boolean isPasswordToggleEnabled;
    private String mHint;
    private boolean isPasswordToggledVisible;
    private int mInputType;
    private int mImeOptions;
    private int mMaxLength;
    protected int mHintColor;
    protected int mTextColor;
    private boolean mHasError;
    private boolean mHasText;
    protected boolean isEditable;
    private boolean isWithoutError;
    private TextChangeListener mTextListener;

    private static final int[] ERROR_STATE = new int[]{R.attr.state_error};
    private static final int[] HAS_TEXT_STATE = new int[]{R.attr.state_has_text};
    private static final int[] DISABLED = new int[]{R.attr.disabled};
    private static final String PASS_EYE_VISIBLE = "PASS_EYE_VISIBLE";
    protected static final String EDITABLE = "EDITABLE";

    public interface TextChangeListener {
        void onTextChanged(String text);
    }

    //region Constructors
    public TDInputField(@NonNull Context context) {
        super(context);
    }

    public TDInputField(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TDInputField(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //endregion

    //region Public methods
    public void focus() {
        if (!mEtInput.hasFocus()) {
            mEtInput.setSelection(mEtInput.length());
        }
        mEtInput.setFocusableInTouchMode(true);
        mEtInput.requestFocus();
        mEtInput.setCursorVisible(true);
        CommonUtils.showSoftInput(getContext(), mEtInput);
    }

    public void setNextInput(TDInputField TDInputField) {
        if (TDInputField == null) {
            setImeActionListener(null);
            return;
        }
        setImeActionListener((textView, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                TDInputField.focus();
                return true;
            }
            return false;
        });
    }

    public void setActionListener(TextView.OnEditorActionListener listener) {
        setImeActionListener(listener);
    }

    public void setText(CharSequence text) {
        mEtInput.setText(text);
    }

    public void setText(@StringRes int resId) {
        mEtInput.setText(resId);
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
        setEditParams();
    }

    public boolean isEditable() {
        return isEditable;
    }

    @Override
    public void clearFocus() {
        String newText = mEtInput.getText().toString().trim();
        if (!newText.equals(mEtInput.getText().toString())) {
            mEtInput.setText(newText);
        }
        CommonUtils.hideSoftKeyboard(mEtInput);
        super.clearFocus();
        mEtInput.setFocusableInTouchMode(false);
        mEtInput.clearFocus();
        mEtInput.setCursorVisible(false);
    }


    public void setIsWithoutError(boolean without) {
        isWithoutError = without;
        mTvError.setVisibility(isWithoutError ? GONE : VISIBLE);
    }

    public void addTextChangedListener(TextWatcher fieldTextWatcher) {
        mEtInput.addTextChangedListener(fieldTextWatcher);
    }

    public void resetImeActionListener() {
        setImeActionListener((textView, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)
                    || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                clearFocus();
                return true;
            }
            return false;
        });
    }

    public void setImeActionListener(TextView.OnEditorActionListener listener) {
        mEtInput.setOnEditorActionListener(listener);
    }

    public void setMaxLength(int maxLength) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        mEtInput.setFilters(FilterArray);
        mEtInput.setHorizontallyScrolling(false);
    }

    public void showError(String error) {
        if (isWithoutError) {
            return;
        }
        if (error == null || error.isEmpty()) {
            hideError();
        }
        mHasError = true;
        mTvError.setVisibility(VISIBLE);
        mTvError.setText(error.toLowerCase());
        refreshDrawableState();
    }

    public void hideError() {
        if (!mHasError) {
            return;
        }
        mHasError = false;
        mTvError.setVisibility(INVISIBLE);
        refreshDrawableState();
    }

    public String getText() {
        return mEtInput.getText().toString();
    }

    public EditText getEditTextView() {
        return mEtInput;
    }

    public void togglePasswordVisibility(boolean shouldSkipAnimations) {
        if (isPasswordToggleEnabled) {
            // Store the current cursor position
            final int selection = mEtInput.getSelectionEnd();

            if (hasPasswordTransformation()) {
                mEtInput.setTransformationMethod(null);
                isPasswordToggledVisible = true;
            } else {
                mEtInput.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                isPasswordToggledVisible = false;
            }

            mIvPasswordToggle.setChecked(isPasswordToggledVisible);
            if (shouldSkipAnimations) {
                mIvPasswordToggle.jumpDrawablesToCurrentState();
            }

            mEtInput.setSelection(selection);
        }
    }

    @Override
    public boolean isFocused() {
        return mEtInput.isFocused();
    }

    public void setHint(String hint) {
        mHint = hint;
        mEtInput.setHint(mHint);
    }

    public void setHintTextColor(@ColorRes int color) {
        mHintColor = ContextCompat.getColor(getContext(), color);
        mEtInput.setHintTextColor(mHintColor);
    }

    public void setTextColor(@ColorRes int color) {
        mTextColor = ContextCompat.getColor(getContext(), color);
        mEtInput.setTextColor(mTextColor);
    }

    public void setImeOptions(int imeOptions) {
        mImeOptions = imeOptions;
        mEtInput.setImeOptions(mImeOptions);
    }

    public void setInputType(int inputType) {
        mInputType = inputType;
        mEtInput.setInputType(mInputType);
    }

    public void setImage(Drawable drawable) {
        mPasswordToggleDrawable = drawable;
        mIvPasswordToggle.setImageDrawable(drawable);
    }

    public void setMaxLines(int lines) {
        mEtInput.setMaxLines(lines);
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && mEtInput.hasFocus()) {
            clearFocus();
            return true;
        }
        return super.dispatchKeyEventPreIme(event);
    }

    public void setTextListener(TextChangeListener listener) {
        mTextListener = listener;
    }

    public void hideCursor() {
        mEtInput.setCursorVisible(false);
    }

    public void clearText(){
        mEtInput.setText("");
    }
    //endregion

    //region Protected methods
    @Override
    protected int getCustomViewLayoutId() {
        return R.layout.input_field;
    }

    @Override
    protected void initViewParams(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().
                    obtainStyledAttributes(attrs, R.styleable.TDInputField, 0, 0);
            mHint = typedArray.getString(R.styleable.TDInputField_hint);
            mHintColor = typedArray.getColor(R.styleable.TDInputField_hintColor, getResources().getColor(R.color.colorTextOnPlaceholder));
            mTextColor = typedArray.getColor(R.styleable.TDInputField_textColor, getResources().getColor(R.color.colorTextOnPrimary));
            mPasswordToggleDrawable = typedArray.getDrawable(R.styleable.TDInputField_passwordToggleDrawable);
            if (mPasswordToggleDrawable != null) {
                isPasswordToggleEnabled = true;
            }
            mInputType = typedArray.getInt(R.styleable.TDInputField_android_inputType, EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);
            mImeOptions = typedArray.getInt(R.styleable.TDInputField_android_imeOptions, 0);
            mMaxLength = typedArray.getInt(R.styleable.TDInputField_maxLength, 64);
            isEditable = typedArray.getBoolean(R.styleable.TDInputField_editable, true);
            isWithoutError = typedArray.getBoolean(R.styleable.TDInputField_withoutError, false);
            typedArray.recycle();
        } else {
            mHintColor = getResources().getColor(R.color.colorTextOnPlaceholder);
            mTextColor = getResources().getColor(R.color.colorTextOnPrimary);
        }
    }

    @Override
    protected void initViewComponents(View view) {
        ButterKnife.bind(this, view);
        mEtInput.setHint(mHint);
        mEtInput.setImeOptions(mImeOptions);
        mEtInput.setInputType(mInputType);
        mEtInput.setTypeface(Typeface.SERIF);
        mEtInput.setTextColor(mTextColor);
        mEtInput.setHintTextColor(mHintColor);
        setMaxLength(mMaxLength);
        mEtInput.setMaxLines(3);
        mEtInput.setHorizontallyScrolling(false);
        setEditParams();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState parcelable = (SavedState) super.onSaveInstanceState();
        Bundle data = new Bundle();
        data.putBoolean(PASS_EYE_VISIBLE, isPasswordToggledVisible);
        data.putBoolean(EDITABLE, isEditable);
        parcelable.setData(data);
        return parcelable;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);

        isPasswordToggledVisible = ((SavedState) state).savedObjects.getBoolean(PASS_EYE_VISIBLE);
        isEditable = ((SavedState) state).savedObjects.getBoolean(EDITABLE);
        mIvPasswordToggle.setChecked(isPasswordToggledVisible);
        if (isPasswordToggledVisible || !isPasswordToggleEnabled) {
            mEtInput.setTransformationMethod(null);
        } else {
            mEtInput.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        }
        setEditParams();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] state = super.onCreateDrawableState(extraSpace + 3);
        if (isEditable) {
            if (mHasError) {
                mergeDrawableStates(state, ERROR_STATE);
            }
            if (mHasText) {
                mergeDrawableStates(state, HAS_TEXT_STATE);
            }
        } else {
            mergeDrawableStates(state, DISABLED);
        }
        return state;
    }
    //endregion

    //region Private methods
    private void setEditParams() {
        if (isEditable) {
            mIvPasswordToggle.setImageDrawable(mPasswordToggleDrawable);
            mIvPasswordToggle.setOnClickListener(
                    view1 -> togglePasswordVisibility(false));
            if (isPasswordToggleEnabled) {
                mIvPasswordToggle.setChecked(false);
                mEtInput.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            }
            mEtInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    hideError();
                    if (isPasswordToggleEnabled) {
                        if (charSequence.length() > 0) {
                            mIvPasswordToggle.setVisibility(VISIBLE);
                        } else {
                            mIvPasswordToggle.setVisibility(INVISIBLE);
                        }
                    }

                    if (charSequence.length() > 0) {
                        setHasText(true);
                    } else {
                        setHasText(false);
                    }
                    if (mTextListener != null) {
                        mTextListener.onTextChanged(charSequence.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            setClickable(true);
            resetImeActionListener();
            setFocusable(true);
            setFocusableInTouchMode(true);
            mEtInput.setOnFocusChangeListener(onFocus);
            mEtInput.setDuplicateParentStateEnabled(true);
            mEtInput.setOnClickListener(v -> {
                if (isClickable()) {
                    focus();
                }
            });
            mEtInput.setFocusable(false);
        } else {
            setOnClickListener(null);
            setClickable(false);
            setFocusable(false);
            setFocusableInTouchMode(false);
            mEtInput.setClickable(false);
            mEtInput.setCursorVisible(false);
            mEtInput.setFocusable(false);
            mEtInput.setFocusableInTouchMode(false);
            mEtInput.setTextColor(getResources().getColor(R.color.colorTextOnPrimary));
            mIvPasswordToggle.setVisibility(GONE);
        }
        if (isWithoutError) {
            mTvError.setVisibility(GONE);
        } else {
            mTvError.setVisibility(mHasError ? VISIBLE : INVISIBLE);
        }
       // CommonUtils.toggleTextViewColors(mEtInput, isEditable);
        refreshDrawableState();
    }

    private boolean hasPasswordTransformation() {
        return mEtInput != null
                && mEtInput.getTransformationMethod() instanceof AsteriskPasswordTransformationMethod;
    }

    private void setHasText(boolean hasText) {
        mHasText = hasText;
        if (isEditable) {
            refreshDrawableState();
        }
    }

    private View.OnFocusChangeListener onFocus = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                hideError();
                mEtInput.setCursorVisible(true);
            } else {
                post(() -> {
                            Context context = getContext();
                            if (context instanceof ContextWrapper) {
                                context = ((ContextWrapper) getContext()).getBaseContext();
                            }
                            if (context instanceof Activity && ((Activity) context).getCurrentFocus() == null) {
                                CommonUtils.hideSoftKeyboard(mEtInput);
                            }
                        }
                );
                mEtInput.setFocusable(false);
                mEtInput.setFocusableInTouchMode(false);
            }
        }
    };
    //endregion
}


package ru.test.todolist.loginScreen;

import android.os.Bundle;
import android.text.Html;
import android.transition.TransitionManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.test.todolist.R;
import ru.test.todolist.TDApplication;
import ru.test.todolist.customViews.TDInputField;
import ru.test.todolist.customViews.TDSelectableButton;
import ru.test.todolist.view.BaseFragment;

import static ru.test.todolist.utils.ValidatorKt.isValidEmail;
import static ru.test.todolist.utils.ValidatorKt.isValidPassword;

public class LoginFragment extends BaseFragment<LoginPresenter> implements LoginView {
    @InjectPresenter
    LoginPresenter mPresenter;
    @BindView(R.id.cl_container)
    ConstraintLayout mClContainer;
    @BindView(R.id.tv_agreement)
    TextView mTvAgreement;
    @BindView(R.id.bt_action)
    Button mBtAction;
    @BindView(R.id.if_email)
    TDInputField mIfEmail;
    @BindView(R.id.if_password)
    TDInputField mIfPassword;
    @BindView(R.id.if_password_confirm)
    TDInputField mIfPasswordConfirm;
    @BindView(R.id.bt_login)
    TDSelectableButton mSbLogin;
    @BindView(R.id.bt_signin)
    TDSelectableButton mSbSignin;
    @BindView(R.id.tv_forgot_password)
    TextView mTvForgotPassword;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        String agreement = getString(R.string.agreement_text);
        mTvAgreement.setText(Html.fromHtml(agreement), TextView.BufferType.SPANNABLE);
        mIfEmail.setNextInput(mIfPassword);
        mIfEmail.setActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    isValidEmail(mIfEmail.getEditTextView(), true);
                    return true;
                }
                return false;
            }
        });
        setStage(SIGNUP_STAGE);
        mSbSignin.setSelected(true);
        mSbLogin.setSelected(false);
        mSbLogin.setParams(getString(R.string.log_in));
        mSbLogin.setOnClickListener(v -> {
            if (mSbSignin.isSelected()) {
                mSbSignin.setSelected(false);
                setStage(LOGIN_STAGE);
            }
            v.setSelected(true);
        });
        mSbSignin.setParams(getString(R.string.sign_up));
        mSbSignin.setOnClickListener(v -> {
            if (mSbLogin.isSelected()) {
                mSbLogin.setSelected(false);
                setStage(SIGNUP_STAGE);
            }
            v.setSelected(true);
        });
    }

    private void setStage(int stage) {
        TransitionManager.beginDelayedTransition(mClContainer);
        switch (stage) {
            case LOGIN_STAGE:
                mBtAction.setText(R.string.log_in);
                mBtAction.setOnClickListener(v -> mPresenter.login());
                mTvForgotPassword.setVisibility(View.VISIBLE);
                mIfPasswordConfirm.setVisibility(View.INVISIBLE);
                mIfPassword.setImeOptions(EditorInfo.IME_ACTION_DONE);
                mIfPassword.setActionListener((v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        mPresenter.login();
                        return true;
                    }
                    return false;
                });
                clearFields();
                break;
            case SIGNUP_STAGE:
                mBtAction.setText(R.string.sign_up);
                mBtAction.setOnClickListener(v -> {
                    signupAfterCheck();
                });
                mTvForgotPassword.setVisibility(View.INVISIBLE);
                mIfPassword.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                mIfPassword.setNextInput(mIfPasswordConfirm);
                mIfPassword.setActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_NEXT) {
                            isValidPassword(mIfPassword.getEditTextView(), true);
                            return true;
                        }
                        return false;
                    }
                });
                mIfPasswordConfirm.setVisibility(View.VISIBLE);
                mIfPasswordConfirm.setActionListener((v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        signupAfterCheck();
                        return true;
                    }
                    return false;
                });
                clearFields();
                break;
        }
    }

    private void clearFields() {
        mIfEmail.clearText();
        mIfPassword.clearText();
        mIfPasswordConfirm.clearText();
    }

    private void signupAfterCheck() {
        if (checkPasswordMatch()) {
            mPresenter.signup();
        } else {
            mIfPasswordConfirm.showError(getString(R.string.password_do_not_match));
        }
    }

    private boolean checkPasswordMatch() {
        return mIfPassword.getText().equals(mIfPasswordConfirm.getText());
    }

    @Override
    @ProvidePresenter
    protected LoginPresenter providePresenter() {
        return TDApplication.getComponent().fragmentComponent().loginPresenter();
    }

    @Override
    protected LoginPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }
}

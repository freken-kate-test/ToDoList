package ru.test.todolist.loginScreen;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ru.test.todolist.presenter.BasePresenter;
import ru.test.todolist.presenter.Empty;

@InjectViewState
public class LoginPresenter extends BasePresenter<LoginView, Empty, Void> {

    @Inject
    public LoginPresenter() {
    }


    public void login() {
        //todo implement some login logic
        getViewState().showError("LOGIN");
    }

    public void signup() {
        //todo implement some signup logic
        getViewState().showError("SIGNUP");
        getViewState().showPreloader(true);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

}

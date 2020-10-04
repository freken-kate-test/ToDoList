package ru.test.todolist.mainScreen;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ru.test.todolist.presenter.BasePresenter;
import ru.test.todolist.presenter.Empty;

@InjectViewState
public class MainPresenter extends BasePresenter<MainView, Empty, Void> {

    public static final String PRESENTER_TAG = "MainPresenter";

    @Inject
    public MainPresenter() {
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

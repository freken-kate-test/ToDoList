package ru.test.todolist.di.modules;

import dagger.Binds;
import dagger.Module;
import ru.test.todolist.di.PerFragment;
import ru.test.todolist.loginScreen.LoginPresenter;
import ru.test.todolist.presenter.BasePresenter;

@Module
public abstract class LoginFragmentModule {

    @Binds
    @PerFragment
    abstract BasePresenter presenter(LoginPresenter loginPresenter);
}

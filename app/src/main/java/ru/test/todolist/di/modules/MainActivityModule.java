package ru.test.todolist.di.modules;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import ru.test.todolist.Navigator;
import ru.test.todolist.di.PerActivity;
import ru.test.todolist.di.PerFragment;
import ru.test.todolist.loginScreen.LoginFragment;
import ru.test.todolist.mainScreen.MainPresenter;
import ru.test.todolist.presenter.BasePresenter;


@Module
public abstract class MainActivityModule {

    @Binds
    @PerActivity
    abstract BasePresenter presenter(MainPresenter mainPresenter);


    @Provides
    @PerActivity
    public static Navigator navigator() {
        return new Navigator();
    }

    @PerFragment
    @ContributesAndroidInjector(modules = {LoginFragmentModule.class})
    abstract LoginFragment loginFragment();

}

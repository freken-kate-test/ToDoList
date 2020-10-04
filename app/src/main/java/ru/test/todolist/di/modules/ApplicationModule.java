package ru.test.todolist.di.modules;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.test.todolist.di.PerActivity;
import ru.test.todolist.mainScreen.MainActivity;

@Module
public interface ApplicationModule {

    @PerActivity
    @ContributesAndroidInjector(modules = {MainActivityModule.class})
    MainActivity mainActivity();
}

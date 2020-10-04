package ru.test.todolist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import ru.test.todolist.di.components.ApplicationComponent;
import ru.test.todolist.di.components.DaggerApplicationComponent;

public class TDApplication extends Application implements HasActivityInjector{
    @Inject
    DispatchingAndroidInjector<Activity> mDispatchingActivityInjector;

    private static ApplicationComponent sApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (sApplicationComponent == null) {
            sApplicationComponent = DaggerApplicationComponent.builder()
                    .context(this)
                    .build();
            sApplicationComponent.inject(this);
        }
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return mDispatchingActivityInjector;
    }
    public static ApplicationComponent getComponent() {
        return sApplicationComponent;
    }
}

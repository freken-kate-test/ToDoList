package ru.test.todolist.di.components;

import android.content.Context;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import ru.test.todolist.TDApplication;
import ru.test.todolist.di.modules.ApplicationModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, ApplicationModule.class})
public interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder context(Context context);

        ApplicationComponent build();
    }

    ActivityComponent activityComponent();

    FragmentComponent fragmentComponent();

    void inject(TDApplication application);
}

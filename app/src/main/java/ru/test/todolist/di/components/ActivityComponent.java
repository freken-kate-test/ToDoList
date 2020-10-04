package ru.test.todolist.di.components;

import dagger.Subcomponent;
import ru.test.todolist.di.PerActivity;
import ru.test.todolist.mainScreen.MainPresenter;

@PerActivity
@Subcomponent
public interface ActivityComponent {
    MainPresenter mainPresenter();
}

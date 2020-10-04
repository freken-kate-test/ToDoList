package ru.test.todolist.di.components;

import dagger.Subcomponent;
import ru.test.todolist.di.PerFragment;
import ru.test.todolist.loginScreen.LoginPresenter;

@PerFragment
@Subcomponent
public interface FragmentComponent {

    LoginPresenter loginPresenter();
}

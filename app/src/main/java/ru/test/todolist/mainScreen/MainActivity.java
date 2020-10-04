package ru.test.todolist.mainScreen;

import android.os.Bundle;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;

import ru.test.todolist.NavigationCommand;
import ru.test.todolist.Navigator;
import ru.test.todolist.R;
import ru.test.todolist.TDApplication;
import ru.test.todolist.view.BaseActivity;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView {
    @InjectPresenter(type = PresenterType.GLOBAL, tag = MainPresenter.PRESENTER_TAG)
    MainPresenter mPresenter;
    @Inject
    Navigator mNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void navigate(NavigationCommand command) {
        mNavigator.navigate(this, R.id.main_host_fragment, command);
    }

    @Override
    @ProvidePresenter(type = PresenterType.GLOBAL, tag = MainPresenter.PRESENTER_TAG)
    protected MainPresenter providePresenter() {
        return TDApplication.getComponent().activityComponent().mainPresenter();
    }

    @Override
    protected MainPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
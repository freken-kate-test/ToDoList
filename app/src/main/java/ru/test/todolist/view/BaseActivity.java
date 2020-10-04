package ru.test.todolist.view;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.arellomobile.mvp.MvpAppCompatActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import ru.test.todolist.presenter.BasePresenter;


/**
 * Базовый класс Activity.
 * Реализует {@link MvpAppCompatActivity} для реализации Mvp с помощью библиотеки Moxy.
 */
public abstract class BaseActivity<Presenter extends BasePresenter> extends MvpAppCompatActivity
        implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> mInjector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPresenter().pause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            getPresenter().destroy();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().resume();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mInjector;
    }

    /**
     * Небходимо для внедрения в Moxy созданного Dagger'ом объекта Presenter
     * необходимо аннотировать реализованный метод аннотацией {@link com.arellomobile.mvp.presenter.ProvidePresenter}
     *
     * @return Presenter для Moxy
     */
    protected abstract Presenter providePresenter();

    protected abstract Presenter getPresenter();
}

package ru.test.todolist.view;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.arellomobile.mvp.MvpAppCompatFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;
import ru.test.todolist.presenter.BasePresenter;

/**
 * Базовый класс Fragment.
 * Реализует {@link MvpAppCompatFragment} для реализации Mvp с помощью библиотеки Moxy.
 */
public abstract class BaseFragment<Presenter extends BasePresenter> extends MvpAppCompatFragment implements HasSupportFragmentInjector, PreloaderHost {

    @Inject
    DispatchingAndroidInjector<Fragment> mInjector;

    private DialogProgress mDialogProgress;
    private int mViewCreationCounter;

    @Override
    public void onAttach(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Perform injection here for M (API 23) due to deprecation of onAttach(*Activity*)
            AndroidSupportInjection.inject(this);
        }
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Perform injection here for versions before M as onAttach(*Context*) did not yet exist
            // This fixes DaggerFragment issue: https://github.com/google/dagger/issues/777
            AndroidSupportInjection.inject(this);
        }
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resolveArguments();
        mViewCreationCounter = 0;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewCreationCounter++;
    }

    public boolean isFirstViewCreation() {
        return mViewCreationCounter == 1;
    }

    @Override
    public void onPause() {
        super.onPause();
        getPresenter().pause();
    }

    @Override
    public void onStop() {
        super.onStop();
      //  CommonUtils.hideSoftKeyboard(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() == null || getActivity().isFinishing()) {
            getPresenter().destroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().resume();
        setNavigationParams();
    }

    public void showError(String error) {

    }

    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        return insets;
    }

    @Override
    public void showPreloader(boolean delayed) {
        if (mDialogProgress == null) {
            mDialogProgress = new DialogProgress();
        }
        if (delayed) {
            mDialogProgress.showDelayed(getChildFragmentManager(), DialogProgress.TAG);
        } else {
            mDialogProgress.show(getChildFragmentManager(), DialogProgress.TAG);
        }
    }

    @Override
    public void hidePreloader() {
        if (mDialogProgress != null) {
            mDialogProgress.dismiss();
        }
    }

    protected void setNavigationParams() {
    }

    protected void resolveArguments() {
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

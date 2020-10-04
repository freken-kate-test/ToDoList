package ru.test.todolist.presenter;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arellomobile.mvp.MvpPresenter;

import java.util.Objects;

import ru.test.todolist.view.MvpViewBase;

/**
 * Базовый класс Presenter в архитектруре MVP.
 *
 * @param <View>   интерфейс View-компонента
 * @param <Entity> Entity, содержащий данные для первичного заполнения экрана (и, возможно, некоторую логику)
 */
public abstract class BasePresenter<View extends MvpViewBase, Params, Entity> extends MvpPresenter<View> implements Presenter {

//    private Params mInitialParams;
//    private boolean mIsInitialDataLoaded;
//
//    /**
//     * Установка объекта с исходными параметрами для загрузки первичных данных View
//     *
//     * @param params параметры
//     */
//    public final void setInitialData(Params params) {
//        if (params != null && (mInitialParams == null || !Objects.equals(mInitialParams, params))) {
//            this.mInitialParams = params;
//            this.mIsInitialDataLoaded = false;
//            if (getInitInteractor() != null) {
//                getInitInteractor().setData(mInitialParams);
//            }
//        }
//    }
//
//    /**
//     * При первом присоединении View к Presenter происходит загрузка данных экрана
//     */
//    @Override
//    protected final void onFirstViewAttach() {
//        if (getInitInteractor() == null) {
//            return;
//        }
//        getInitInteractor().setData(mInitialParams);
//        getInitInteractor().setStreamEventsHandler(notification -> {
//            if (notification.isOnComplete()) {
//                mIsInitialDataLoaded = true;
//            }
//        });
//        loadInitialData();
//    }
//
//    public final void loadInitialData() {
//        if (hasInitialData() && getInitInteractor() != null && getInitSubscriber() != null) {
//            getInitInteractor().execute(getInitSubscriber());
//        }
//    }
//
//    @Override
//    @CallSuper
//    public void resume() {
//        if (!mIsInitialDataLoaded) {
//            loadInitialData();
//        }
//    }
//
//    @Override
//    public void pause() {
//    }
//
//    @Override
//    @CallSuper
//    public void destroy() {
//        onDestroy();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (getInitInteractor() != null) {
//            getInitInteractor().unsubscribe();
//        }
//    }
//
//    public void handleError(@NonNull Throwable e) {
//        String error;
//        if (e instanceof ResponseException) {
//            error = ((ResponseException) e).getErrorResult().getMessage();
//        } else {
//            error = e.getMessage();
//        }
//        getViewState().hidePreloader();
//        getViewState().showError(error);
//    }
//
//    public void handleError(@NonNull ErrorResult e) {
//        getViewState().hidePreloader();
//        getViewState().showError(e.getMessage());
//    }
//
//    protected void reset() {
//        mIsInitialDataLoaded = false;
//    }
//
//    @Override
//    public boolean onBackPressed() {
//        return false;
//    }
//
//    /**
//     * @return Interactor для загрузки первичных данных экрана
//     */
//    @Nullable
//    protected Interactor<Params, Entity> getInitInteractor() {
//        return null;
//    }
//
//    /**
//     * @return Subscriber для загрузки первичных данных экрана
//     */
//    @Nullable
//    protected DefaultSubscriber<Entity> getInitSubscriber() {
//        return null;
//    }
//
//    private boolean hasInitialData() {
//        if (mInitialParams != null) {
//            return true;
//        }
//        try {
//            mInitialParams = (Params) Empty.CHECK;
//            return true;
//        } catch (Exception e) {
//        }
//        return false;
//    }
}

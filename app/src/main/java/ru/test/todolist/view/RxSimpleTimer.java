package ru.test.todolist.view;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class RxSimpleTimer {

    private Consumer<Long> mOnNextConsumer;

    public void subscribe(Consumer<Long> onNextConsumer, int delay) {
        mOnNextConsumer = onNextConsumer;
        Observable.timer(delay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (mOnNextConsumer != null) {
                        mOnNextConsumer.accept(aLong);
                    }
                });
    }

    public void dispose() {
        mOnNextConsumer = null;
    }
}

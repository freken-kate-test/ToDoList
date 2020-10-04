package ru.test.todolist.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.test.todolist.NavigationCommand;


/**
 * Базовый интефейс View в архитектуре MVP
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface MvpViewBase extends MvpView {

    @StateStrategyType(SkipStrategy.class)
    default void showError(String error) {
    }

    @StateStrategyType(SkipStrategy.class)
    default void navigate(NavigationCommand command) {
    }

    @StateStrategyType(OneExecutionStateStrategy.class)
    default void showPreloader(boolean delayed) {
    }

    @StateStrategyType(OneExecutionStateStrategy.class)
    default void hidePreloader() {
    }
}

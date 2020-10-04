package ru.test.todolist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;

import java.lang.ref.WeakReference;
import java.util.Iterator;

public class Navigator {

    public Navigator() {
    }

    //region Public methods
    public void navigate(View view, @NonNull NavigationCommand command) {
        navigateInternally(view, command);
    }

    public void navigate(Activity activity, int resId, @NonNull NavigationCommand command) {
        View view = activity.findViewById(resId);
        navigateInternally(view, command);
    }

    public void navigate(@NonNull NavController controller, @NonNull NavigationCommand command) {
        navigateInternally(controller, command);
    }
    //endregion

    //region Private methods
    private void navigateInternally(View view, @NonNull NavigationCommand command) {
        NavController controller = findNavController(view, command.getDestinationId());
        if (controller == null) {
            return;
        }
        navigateInternally(controller, command);
    }

    private void navigateInternally(@NonNull NavController controller, @NonNull NavigationCommand command) {
        if (!hasDestination(controller, command.getDestinationId())) {
            return;
        }
        controller.navigate(command.getDestinationId(), command.getArguments(), getNavOptions(command));
    }

    private NavOptions getNavOptions(NavigationCommand command) {
        NavOptions.Builder builder = new NavOptions.Builder();
        if (command.isPopStackNeeded()) {
            builder.setPopUpTo(command.getPopTargetId(), command.isPopInclusive());
        }
        return builder.build();
    }

    private NavController findNavController(View view, int resId) {
        while (view != null) {
            NavController controller = getViewNavController(view);
            if (controller != null) {
                if (hasDestination(controller, resId)) {
                    return controller;
                }
            }
            ViewParent parent = view.getParent();
            view = parent instanceof View ? (View) parent : null;
        }
        return null;
    }

    private static NavController getViewNavController(@NonNull View view) {
        Object tag = view.getTag(R.id.nav_controller_view_tag);
        NavController controller = null;
        if (tag instanceof WeakReference) {
            controller = ((WeakReference<NavController>) tag).get();
        } else if (tag instanceof NavController) {
            controller = (NavController) tag;
        }
        return controller;
    }

    /**
     * Check if controller contains navigation node
     *
     * @param controller NavController to find destination in
     * @param resId      id of an action or a destination
     */
    private boolean hasDestination(NavController controller, int resId) {
        if (controller.getCurrentDestination() != null) {
            Iterator<NavDestination> iterator = controller.getGraph().iterator();
            NavDestination destination;
            while (iterator.hasNext()) {
                destination = iterator.next();
                if (destination.getId() == resId || destination.getAction(resId) != null) {
                    return true;
                }
            }
        }
        return false;
    }
//endregion
}

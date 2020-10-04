package ru.test.todolist.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public final class CommonUtils {
    public static void hideSoftKeyboard(Activity activity) {
        if (activity == null) {
            return;
        }
        View focusedView = activity.getCurrentFocus();
        if (focusedView == null) {
            focusedView = new View(activity);
            focusedView.requestFocus();
        }
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                focusedView.getWindowToken(), 0);
    }

    public static void hideSoftKeyboard(View inputField) {
        InputMethodManager imm = (InputMethodManager) inputField.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputField.getWindowToken(), 0);
    }

    public static void showSoftInput(Context context, View inputView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!imm.showSoftInput(inputView, InputMethodManager.SHOW_IMPLICIT)) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static int convertDpToPixels(float dp) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * scale);
    }

    public static int convertDpToPixels(int dp) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * scale);
    }

    public static float convertPixelsToDp(int pixels) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return pixels / scale;
    }

    public static int getResIdByName(Context context, String resName, String defType) {
        return context.getResources().getIdentifier(resName, defType, context.getPackageName());
    }
}

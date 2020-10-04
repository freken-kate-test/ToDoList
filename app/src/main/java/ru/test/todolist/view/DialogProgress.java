package ru.test.todolist.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.concurrent.locks.ReentrantLock;

import ru.test.todolist.R;

public class DialogProgress extends DialogFragment {

    private RxSimpleTimer mTimer;
    private ReentrantLock mVisibilityLock;

    private boolean shouldBeClosed;

    public static final String TAG = DialogProgress.class.getSimpleName();
    private static final int DEFAULT_DELAY_MS = 500;

    //region Public methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.progress_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (shouldBeClosed) {
            dismiss();
        }
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setCancelable(false);
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if ((keyCode == KeyEvent.KEYCODE_BACK) && event.getAction() == KeyEvent.ACTION_UP) {
                dismiss();
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
                return true;
            } else {
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        getTimer().dispose();
        shouldBeClosed = false;
        super.onDestroyView();
    }

    public boolean isShowing() {
        Dialog dialog = getDialog();
        return dialog != null && dialog.isShowing() && isAdded() && !isRemoving();
    }

    public void showDelayed(FragmentManager manager, String tag) {
        getTimer().dispose();
        if (getVisibilityLock().isLocked()) {
            return;
        }
        getTimer().subscribe(aLong -> show(manager, tag), DEFAULT_DELAY_MS);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        getTimer().dispose();
        if (!getVisibilityLock().isLocked() && !isShowing()) {
            shouldBeClosed = false;
            super.show(manager, tag);
        }
    }

    @Override
    public void dismiss() {
        getTimer().dispose();
        if (getFragmentManager() == null) {
            shouldBeClosed = true;
            return;
        }
        shouldBeClosed = false;
        getVisibilityLock().lock();
        super.dismiss();
        getVisibilityLock().unlock();
    }
    //endregion

    //region Private methods
    private RxSimpleTimer getTimer() {
        if (mTimer == null) {
            mTimer = new RxSimpleTimer();
        }
        return mTimer;
    }

    private ReentrantLock getVisibilityLock() {
        if (mVisibilityLock == null) {
            mVisibilityLock = new ReentrantLock();
        }
        return mVisibilityLock;
    }
    //endregion
}

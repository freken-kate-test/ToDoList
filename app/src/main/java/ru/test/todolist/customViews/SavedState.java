package ru.test.todolist.customViews;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;

public class SavedState extends View.BaseSavedState {
    public SparseArray childrenStates;
    public Bundle savedObjects;

    SavedState(Parcelable superState) {
        super(superState);
    }

    private SavedState(Parcel in, ClassLoader classLoader) {
        super(in);
        childrenStates = in.readSparseArray(classLoader);
        savedObjects = in.readBundle(classLoader);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeSparseArray(childrenStates);
        out.writeBundle(savedObjects);
    }

    public void setData(Bundle bundle) {
        this.savedObjects = bundle;
    }

    public static final ClassLoaderCreator<SavedState> CREATOR
            = new ClassLoaderCreator<SavedState>() {
        @Override
        public SavedState createFromParcel(Parcel source, ClassLoader loader) {
            return new SavedState(source, loader);
        }

        @Override
        public SavedState createFromParcel(Parcel source) {
            return createFromParcel(source, null);
        }

        public SavedState[] newArray(int size) {
            return new SavedState[size];
        }
    };
}

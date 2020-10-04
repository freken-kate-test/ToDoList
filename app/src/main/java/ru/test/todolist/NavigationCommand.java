package ru.test.todolist;

import android.os.Bundle;

import androidx.annotation.NonNull;

public class NavigationCommand {
    private int mDestinationId;
    private Bundle mArgs;
    private boolean isPopStackNeeded = false;
    private int mPopupTargetId;
    private boolean isPopInclusive = false;

    private NavigationCommand() {
        mArgs = new Bundle();
    }

    public int getDestinationId() {
        return mDestinationId;
    }

    public Bundle getArguments() {
        return mArgs;
    }

    public boolean isPopStackNeeded() {
        return isPopStackNeeded;
    }

    public int getPopTargetId() {
        return mPopupTargetId;
    }

    public boolean isPopInclusive() {
        return isPopInclusive;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private NavigationCommand mCommand;

        private Builder() {
            mCommand = new NavigationCommand();
        }

        public NavigationCommand build() {
            return mCommand;
        }

        public NavigationCommand buildWithDestinationId(int destinationId) {
            mCommand.mDestinationId = destinationId;
            return mCommand;
        }

        public Builder createFromCommand(NavigationCommand command) {
            mCommand.mDestinationId = command.mDestinationId;
            mCommand.mArgs = command.mArgs;
            mCommand.isPopStackNeeded = command.isPopStackNeeded;
            mCommand.mPopupTargetId = command.mPopupTargetId;
            mCommand.isPopInclusive = command.isPopInclusive;
            return this;
        }

        public Builder setPopParams(int targetId, boolean isInclusive) {
            mCommand.isPopStackNeeded = true;
            mCommand.mPopupTargetId = targetId;
            mCommand.isPopInclusive = isInclusive;
            return this;
        }

        public Builder setDestinationId(int destId) {
            mCommand.mDestinationId = destId;
            return this;
        }

        public Builder setArguments(@NonNull Bundle args) {
            mCommand.mArgs = args;
            return this;
        }

        public Builder addArguments(Bundle args) {
            mCommand.mArgs.putAll(args);
            return this;
        }

        public Builder addArgument(@NonNull String key, int arg) {
            mCommand.mArgs.putInt(key, arg);
            return this;
        }

        public Builder addArgument(@NonNull String key, long arg) {
            mCommand.mArgs.putLong(key, arg);
            return this;
        }

        public Builder addArgument(@NonNull String key, float arg) {
            mCommand.mArgs.putFloat(key, arg);
            return this;
        }

        public Builder addArgument(@NonNull String key, boolean arg) {
            mCommand.mArgs.putBoolean(key, arg);
            return this;
        }

        public Builder addArgument(@NonNull String key, String arg) {
            mCommand.mArgs.putString(key, arg);
            return this;
        }
    }
}

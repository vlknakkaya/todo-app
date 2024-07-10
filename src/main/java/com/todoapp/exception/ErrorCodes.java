package com.todoapp.exception;

public final class ErrorCodes {

    public static final int USER_NOT_FOUND = 1;
    public static final int TASK_NOT_FOUND = 2;
    public static final int UNKNOWN_TASK_STATUS = 3;
    public static final int AUTHENTICATION_FAILED = 4;

    private ErrorCodes() {
        throw new IllegalStateException("Utility class");
    }

}

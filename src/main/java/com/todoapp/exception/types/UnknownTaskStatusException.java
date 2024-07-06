package com.todoapp.exception.types;

import com.todoapp.exception.ErrorCodes;
import com.todoapp.exception.ToDoAppException;
import org.springframework.http.HttpStatus;

public class UnknownTaskStatusException extends ToDoAppException {

    private final String input;

    public UnknownTaskStatusException(String input) {
        this.input = input;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.UNKNOWN_TASK_STATUS;
    }

    @Override
    public String getErrorMessage() {
        return String.format("Unknown Task Status: %s", input);
    }

    @Override
    protected HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}

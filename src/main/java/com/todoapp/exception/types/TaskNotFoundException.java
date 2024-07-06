package com.todoapp.exception.types;

import com.todoapp.exception.ErrorCodes;
import com.todoapp.exception.ToDoAppException;
import org.springframework.http.HttpStatus;

public class TaskNotFoundException extends ToDoAppException {

    private final String field;
    private final Object value;

    public TaskNotFoundException(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    @Override
    protected int getErrorCode() {
        return ErrorCodes.TASK_NOT_FOUND;
    }

    @Override
    protected String getErrorMessage() {
        return String.format("Task not found with '%s': '%s'", field, value);
    }

    @Override
    protected HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}

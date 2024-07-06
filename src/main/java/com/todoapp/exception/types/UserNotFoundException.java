package com.todoapp.exception.types;

import com.todoapp.exception.ErrorCodes;
import com.todoapp.exception.ToDoAppException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ToDoAppException {

    private final String field;
    private final Object value;

    public UserNotFoundException(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.USER_NOT_FOUND;
    }

    @Override
    public String getErrorMessage() {
        return String.format("User not found with '%s': '%s'", field, value);
    }

    @Override
    protected HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

}

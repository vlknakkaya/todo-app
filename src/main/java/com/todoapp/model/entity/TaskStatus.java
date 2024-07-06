package com.todoapp.model.entity;

import com.todoapp.exception.types.UnknownTaskStatusException;

public enum TaskStatus {

    TO_DO("To Do"),
    IN_PROGRESS("In Progress"),
    DONE("Done");

    private final String name;

    TaskStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static TaskStatus of(String name) throws UnknownTaskStatusException {
        for (TaskStatus taskStatus : TaskStatus.values()) {
            if (taskStatus.getName().equals(name)) {
                return taskStatus;
            }
        }

        throw new UnknownTaskStatusException(name);
    }

}

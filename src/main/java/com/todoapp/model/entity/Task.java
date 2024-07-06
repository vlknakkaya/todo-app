package com.todoapp.model.entity;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

@Document
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    private String id;

    @Field
    @NotNull
    private String title;

    @Field
    @NotNull
    private String description;

    @Field
    @NotNull
    private TaskStatus status;

    @Field
    @NotNull
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public @NotNull String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    public @NotNull String getDescription() {
        return description;
    }

    public void setDescription(@NotNull String description) {
        this.description = description;
    }

    public @NotNull TaskStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull TaskStatus status) {
        this.status = status;
    }

    public @NotNull User getUser() {
        return user;
    }

    public void setUser(@NotNull User user) {
        this.user = user;
    }
}

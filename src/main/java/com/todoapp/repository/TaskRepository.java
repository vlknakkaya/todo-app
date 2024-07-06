package com.todoapp.repository;

import com.todoapp.model.entity.Task;
import com.todoapp.model.entity.TaskStatus;
import com.todoapp.model.entity.User;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CouchbaseRepository<Task, String> {

    List<Task> findByTitleContainingIgnoreCase(String title);

    List<Task> findByDescriptionContainingIgnoreCase(String description);

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByUser(User user);

}

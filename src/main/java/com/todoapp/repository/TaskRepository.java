package com.todoapp.repository;

import com.todoapp.model.entity.Task;
import com.todoapp.model.entity.TaskStatus;
import com.todoapp.model.entity.User;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends CouchbaseRepository<Task, String> {

    List<Task> findByUserAndTitleContainingIgnoreCase(User user, String title);

    List<Task> findByUserAndDescriptionContainingIgnoreCase(User user, String description);

    List<Task> findByUserAndStatus(User user, TaskStatus status);

    Optional<Task> findByUserAndId(User user, String id);

    List<Task> findByUser(User user);

}

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

    List<Task> findByUserIdAndTitleContainingIgnoreCase(String userId, String title);

    List<Task> findByUserIdAndDescriptionContainingIgnoreCase(String userId, String description);

    List<Task> findByUserIdAndStatus(String userId, TaskStatus status);

    Optional<Task> findByUserIdAndId(String userId, String id);

    List<Task> findByUserId(String userId);

}

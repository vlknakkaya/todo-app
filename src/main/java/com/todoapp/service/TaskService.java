package com.todoapp.service;

import com.todoapp.exception.types.TaskNotFoundException;
import com.todoapp.exception.types.UnknownTaskStatusException;
import com.todoapp.model.entity.Task;
import com.todoapp.model.entity.TaskStatus;
import com.todoapp.model.entity.User;
import com.todoapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findByTitleContainingIgnoreCase(String title) {
        return taskRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Task> findByDescriptionContainingIgnoreCase(String description) {
        return taskRepository.findByDescriptionContainingIgnoreCase(description);
    }

    public List<Task> findByStatus(String status) throws UnknownTaskStatusException {
        return taskRepository.findByStatus(TaskStatus.of(status));
    }

    public List<Task> findAllForLoggedUser() {
        User loggedUser = null; // TODO: set user
        return taskRepository.findByUser(loggedUser);
    }

    public Task getById(String id) throws TaskNotFoundException {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("id", id));
    }

    public Task create(Task task) {
        task.setUser(null); // TODO: set user
        return taskRepository.save(task);
    }

    public Task update(String id, Task task) throws TaskNotFoundException {
        Task foundedTask = getById(id);

        if (task.getTitle() != null) {
            foundedTask.setTitle(task.getTitle());
        }
        if (task.getDescription() != null) {
            foundedTask.setDescription(task.getDescription());
        }
        if (task.getStatus() != null) {
            foundedTask.setStatus(task.getStatus());
        }

        return taskRepository.save(foundedTask);
    }

    public void deleteById(String id) {
        taskRepository.deleteById(id);
    }

    public Task changeStatus(String id, String newStatus) throws TaskNotFoundException, UnknownTaskStatusException {
        Task foundedTask = getById(id);
        foundedTask.setStatus(TaskStatus.of(newStatus));

        return taskRepository.save(foundedTask);
    }

}

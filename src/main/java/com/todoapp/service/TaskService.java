package com.todoapp.service;

import com.todoapp.exception.types.TaskNotFoundException;
import com.todoapp.exception.types.UnknownTaskStatusException;
import com.todoapp.model.dto.UpdateTaskRequestDTO;
import com.todoapp.model.entity.Task;
import com.todoapp.model.entity.TaskStatus;
import com.todoapp.repository.TaskRepository;
import com.todoapp.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final AuthService authService;

    @Autowired
    public TaskService(TaskRepository taskRepository, AuthService authService) {
        this.taskRepository = taskRepository;
        this.authService = authService;
    }

    public List<Task> findByTitleContainingIgnoreCase(String title) {
        return taskRepository.findByUserIdAndTitleContainingIgnoreCase(authService.getLoggedUser().getId(), title);
    }

    public List<Task> findByDescriptionContainingIgnoreCase(String description) {
        return taskRepository.findByUserIdAndDescriptionContainingIgnoreCase(authService.getLoggedUser().getId(), description);
    }

    public List<Task> findByStatus(String status) throws UnknownTaskStatusException {
        return taskRepository.findByUserIdAndStatus(authService.getLoggedUser().getId(), TaskStatus.of(status));
    }

    public List<Task> findAllForLoggedUser() {
        return taskRepository.findByUserId(authService.getLoggedUser().getId());
    }

    public Task getById(String id) throws TaskNotFoundException {
        return taskRepository.findByUserIdAndId(authService.getLoggedUser().getId(), id)
                .orElseThrow(() -> new TaskNotFoundException("id", id));
    }

    public Task create(Task task) {
        task.setUser(authService.getLoggedUser());

        return taskRepository.save(task);
    }

    public Task update(String id, UpdateTaskRequestDTO updatedTask) throws TaskNotFoundException {
        Task foundedTask = getById(id);

        if (updatedTask.getTitle() != null) {
            foundedTask.setTitle(updatedTask.getTitle());
        }
        if (updatedTask.getDescription() != null) {
            foundedTask.setDescription(updatedTask.getDescription());
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

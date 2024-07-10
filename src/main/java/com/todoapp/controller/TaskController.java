package com.todoapp.controller;

import com.todoapp.model.dto.TaskDTO;
import com.todoapp.model.dto.UpdateTaskRequestDTO;
import com.todoapp.model.entity.Task;
import com.todoapp.model.mapper.TaskMapper;
import com.todoapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getById(@PathVariable String id) {
        Task task = taskService.getById(id);

        return ResponseEntity.ok(taskMapper.toDto(task));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<TaskDTO>> searchByTitle(@PathVariable String title) {
        List<Task> foundedTasks = taskService.findByTitleContainingIgnoreCase(title);

        return ResponseEntity.ok(taskMapper.toDtos(foundedTasks));
    }

    @GetMapping("/description/{description}")
    public ResponseEntity<List<TaskDTO>> searchByDescription(@PathVariable String description) {
        List<Task> foundedTasks = taskService.findByDescriptionContainingIgnoreCase(description);

        return ResponseEntity.ok(taskMapper.toDtos(foundedTasks));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskDTO>> getByStatus(@PathVariable String status) {
        List<Task> foundedTasks = taskService.findByStatus(status);

        return ResponseEntity.ok(taskMapper.toDtos(foundedTasks));
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAll() {
        List<Task> foundedTasks = taskService.findAllForLoggedUser();

        return ResponseEntity.ok(taskMapper.toDtos(foundedTasks));
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        Task task = taskService.create(taskMapper.toEntity(taskDTO));

        return ResponseEntity.ok(taskMapper.toDto(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable String id, @RequestBody UpdateTaskRequestDTO updateTaskRequestDTO) {
        Task task = taskService.update(id, updateTaskRequestDTO);

        return ResponseEntity.ok(taskMapper.toDto(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTask(@PathVariable String id) {
        taskService.deleteById(id);

        return ResponseEntity.ok(Boolean.TRUE);
    }

    @PutMapping("/{id}/{status}")
    public ResponseEntity<TaskDTO> changeStatus(@PathVariable String id, @PathVariable String status) {
        Task task = taskService.changeStatus(id, status);

        return ResponseEntity.ok(taskMapper.toDto(task));
    }

}

package com.todoapp.service;

import com.todoapp.exception.types.TaskNotFoundException;
import com.todoapp.exception.types.UnknownTaskStatusException;
import com.todoapp.model.entity.Task;
import com.todoapp.model.entity.TaskStatus;
import com.todoapp.model.entity.User;
import com.todoapp.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Test
    void test_findByTitleContainingIgnoreCase() {
        taskService.findByTitleContainingIgnoreCase(any());

        verify(taskRepository, times(1)).findByTitleContainingIgnoreCase(any());
    }

    @Test
    void test_findByDescriptionContainingIgnoreCase() {
        taskService.findByDescriptionContainingIgnoreCase(any());

        verify(taskRepository, times(1)).findByDescriptionContainingIgnoreCase(any());
    }

    @Test
    void test_findByStatus() {
        taskService.findByStatus(TaskStatus.DONE.getName());

        verify(taskRepository, times(1)).findByStatus(any());
    }

    @Test
    void test_findByStatus_unknownStatus() {
        assertThrows(UnknownTaskStatusException.class, () -> taskService.findByStatus("xyz"));
    }

    @Test
    void test_findAllForLoggedUser() {
        taskService.findAllForLoggedUser();

        verify(taskRepository, times(1)).findByUser(any());
    }

    @Test
    void test_getById() {
        when(taskRepository.findById(any())).thenReturn(Optional.of(dummyTask()));

        taskService.getById(any());

        verify(taskRepository, times(1)).findById(any());
    }

    @Test
    void test_getById_notFound() {
        assertThrows(TaskNotFoundException.class, () -> taskService.getById(any()));
    }

    @Test
    void test_create() {
        taskService.create(dummyTask());

        verify(taskRepository, times(1)).save(any());
    }

    @Test
    void test_update() {
        when(taskRepository.findById(any())).thenReturn(Optional.of(dummyTask()));

        taskService.update("123456", dummyTask());

        verify(taskRepository, times(1)).findById(any());
        verify(taskRepository, times(1)).save(any());
    }

    @Test
    void test_update_notFound() {
        assertThrows(TaskNotFoundException.class, () -> taskService.update("123456", dummyTask()));
    }

    @Test
    void test_deleteById() {
        taskService.deleteById(any());

        verify(taskRepository, times(1)).deleteById(any());
    }

    @Test
    void test_changeStatus() {
        when(taskRepository.findById(any())).thenReturn(Optional.of(dummyTask()));

        taskService.changeStatus("12345", TaskStatus.DONE.getName());

        verify(taskRepository, times(1)).findById(any());
        verify(taskRepository, times(1)).save(any());
    }

    @Test
    void test_changeStatus_notFound() {
        assertThrows(TaskNotFoundException.class, () -> taskService.changeStatus("123456", TaskStatus.DONE.getName()));
    }

    @Test
    void test_changeStatus_unknownStatus() {
        when(taskRepository.findById(any())).thenReturn(Optional.of(dummyTask()));

        assertThrows(UnknownTaskStatusException.class, () -> taskService.changeStatus("123456", "xyz"));
    }

    private Task dummyTask() {
        Task task = new Task();
        task.setId("12345");
        task.setTitle("title");
        task.setDescription("description");
        task.setStatus(TaskStatus.TO_DO);
        task.setUser(new User());

        return task;
    }

}

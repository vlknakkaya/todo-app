package com.todoapp.service;

import com.todoapp.exception.types.TaskNotFoundException;
import com.todoapp.exception.types.UnknownTaskStatusException;
import com.todoapp.model.dto.UpdateTaskRequestDTO;
import com.todoapp.model.entity.Task;
import com.todoapp.model.entity.TaskStatus;
import com.todoapp.model.entity.User;
import com.todoapp.repository.TaskRepository;
import com.todoapp.service.auth.AuthService;
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

    @Mock
    private AuthService authService;

    @Test
    void test_findByTitleContainingIgnoreCase() {
        when(authService.getLoggedUser()).thenReturn(dummyUser());

        taskService.findByTitleContainingIgnoreCase("title");

        verify(authService, times(1)).getLoggedUser();
        verify(taskRepository, times(1)).findByUserIdAndTitleContainingIgnoreCase(any(), any());
    }

    @Test
    void test_findByDescriptionContainingIgnoreCase() {
        when(authService.getLoggedUser()).thenReturn(dummyUser());

        taskService.findByDescriptionContainingIgnoreCase("description");

        verify(authService, times(1)).getLoggedUser();
        verify(taskRepository, times(1)).findByUserIdAndDescriptionContainingIgnoreCase(any(), any());
    }

    @Test
    void test_findByStatus() {
        when(authService.getLoggedUser()).thenReturn(dummyUser());

        taskService.findByStatus(TaskStatus.DONE.getName());

        verify(authService, times(1)).getLoggedUser();
        verify(taskRepository, times(1)).findByUserIdAndStatus(any(), any());
    }

    @Test
    void test_findByStatus_unknownStatus() {
        when(authService.getLoggedUser()).thenReturn(dummyUser());

        assertThrows(UnknownTaskStatusException.class, () -> taskService.findByStatus("xyz"));
    }

    @Test
    void test_findAllForLoggedUser() {
        when(authService.getLoggedUser()).thenReturn(dummyUser());

        taskService.findAllForLoggedUser();

        verify(authService, times(1)).getLoggedUser();
        verify(taskRepository, times(1)).findByUserId(any());
    }

    @Test
    void test_getById() {
        User user = dummyUser();
        when(authService.getLoggedUser()).thenReturn(user);
        when(taskRepository.findByUserIdAndId(any(), any())).thenReturn(Optional.of(dummyTask()));

        taskService.getById("123");

        verify(taskRepository, times(1)).findByUserIdAndId(user.getId(), "123");
    }

    @Test
    void test_getById_notFound() {
        when(authService.getLoggedUser()).thenReturn(dummyUser());

        assertThrows(TaskNotFoundException.class, () -> taskService.getById("123"));
    }

    @Test
    void test_create() {
        when(authService.getLoggedUser()).thenReturn(dummyUser());

        taskService.create(dummyTask());

        verify(authService, times(1)).getLoggedUser();
        verify(taskRepository, times(1)).save(any());
    }

    @Test
    void test_update() {
        UpdateTaskRequestDTO updateTaskRequestDTO = new UpdateTaskRequestDTO("title", "description");

        when(authService.getLoggedUser()).thenReturn(dummyUser());
        when(taskRepository.findByUserIdAndId(any(), any())).thenReturn(Optional.of(dummyTask()));

        taskService.update("123456", updateTaskRequestDTO);

        verify(authService, times(1)).getLoggedUser();
        verify(taskRepository, times(1)).findByUserIdAndId(any(), any());
        verify(taskRepository, times(1)).save(any());
    }

    @Test
    void test_update_notFound() {
        when(authService.getLoggedUser()).thenReturn(dummyUser());

        UpdateTaskRequestDTO updateTaskRequestDTO = new UpdateTaskRequestDTO("title", "description");

        assertThrows(TaskNotFoundException.class, () -> taskService.update("123456", updateTaskRequestDTO));
    }

    @Test
    void test_deleteById() {
        taskService.deleteById(any());

        verify(taskRepository, times(1)).deleteById(any());
    }

    @Test
    void test_changeStatus() {
        when(authService.getLoggedUser()).thenReturn(dummyUser());
        when(taskRepository.findByUserIdAndId(any(), any())).thenReturn(Optional.of(dummyTask()));

        taskService.changeStatus("12345", TaskStatus.DONE.getName());

        verify(authService, times(1)).getLoggedUser();
        verify(taskRepository, times(1)).findByUserIdAndId(any(), any());
        verify(taskRepository, times(1)).save(any());
    }

    @Test
    void test_changeStatus_notFound() {
        when(authService.getLoggedUser()).thenReturn(dummyUser());

        assertThrows(TaskNotFoundException.class, () -> taskService.changeStatus("123456", TaskStatus.DONE.getName()));
    }

    @Test
    void test_changeStatus_unknownStatus() {
        when(authService.getLoggedUser()).thenReturn(dummyUser());
        when(taskRepository.findByUserIdAndId(any(), any())).thenReturn(Optional.of(dummyTask()));

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

    private User dummyUser() {
        User user = new User();
        user.setId("12345");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setUsername("username");
        user.setPassword("password");

        return user;
    }

}

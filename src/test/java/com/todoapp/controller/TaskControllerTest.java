package com.todoapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoapp.exception.ErrorCodes;
import com.todoapp.exception.types.TaskNotFoundException;
import com.todoapp.exception.types.UnknownTaskStatusException;
import com.todoapp.model.dto.TaskDTO;
import com.todoapp.model.dto.UpdateTaskRequestDTO;
import com.todoapp.model.entity.Task;
import com.todoapp.model.entity.TaskStatus;
import com.todoapp.model.entity.User;
import com.todoapp.model.mapper.TaskMapper;
import com.todoapp.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Mock
    private TaskMapper taskMapper;

    @Test
    void test_getById() throws Exception {
        Task task = dummyEntity();

        when(taskService.getById(task.getId())).thenReturn(task);

        mockMvc.perform(
                        get("/tasks/{id}", task.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(task.getId()));

        verify(taskService, times(1)).getById(task.getId());
    }

    @Test
    void test_getById_notFound() throws Exception {
        when(taskService.getById(any())).thenThrow(TaskNotFoundException.class);

        mockMvc.perform(
                        get("/tasks/{id}", "123")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(ErrorCodes.TASK_NOT_FOUND));
    }

    @Test
    void test_searchByTitle() throws Exception {
        Task task = dummyEntity();

        when(taskService.findByTitleContainingIgnoreCase(task.getTitle())).thenReturn(Collections.singletonList(task));

        mockMvc.perform(
                        get("/tasks/title/{title}", task.getTitle())
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.*.title").value(task.getTitle()));

        verify(taskService, times(1)).findByTitleContainingIgnoreCase(task.getTitle());
    }

    @Test
    void test_searchByTitle_notFound() throws Exception {
        when(taskService.findByTitleContainingIgnoreCase(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(
                        get("/tasks/title/{title}", "test")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(taskService, times(1)).findByTitleContainingIgnoreCase(any());
    }

    @Test
    void test_searchByDescription() throws Exception {
        Task task = dummyEntity();

        when(taskService.findByDescriptionContainingIgnoreCase(task.getDescription())).thenReturn(Collections.singletonList(task));

        mockMvc.perform(
                        get("/tasks/description/{description}", task.getDescription())
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.*.description").value(task.getDescription()));

        verify(taskService, times(1)).findByDescriptionContainingIgnoreCase(task.getDescription());
    }

    @Test
    void test_searchByDescription_notFound() throws Exception {
        when(taskService.findByDescriptionContainingIgnoreCase(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(
                        get("/tasks/description/{description}", "test")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(taskService, times(1)).findByDescriptionContainingIgnoreCase(any());
    }

    @Test
    void test_getByStatus() throws Exception {
        Task task = dummyEntity();

        when(taskService.findByStatus(task.getStatus().getName())).thenReturn(Collections.singletonList(task));

        mockMvc.perform(
                        get("/tasks/status/{status}", task.getStatus().getName())
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.*.status").value(task.getStatus().getName()));

        verify(taskService, times(1)).findByStatus(task.getStatus().getName());
    }

    @Test
    void test_getByStatus_notFound() throws Exception {
        when(taskService.findByStatus(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(
                        get("/tasks/status/{status}", "test")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(taskService, times(1)).findByStatus(any());
    }

    @Test
    void test_getByStatus_unknownStatus() throws Exception {
        when(taskService.findByStatus(any())).thenThrow(UnknownTaskStatusException.class);

        mockMvc.perform(
                        get("/tasks/status/{status}", "xyz")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(ErrorCodes.UNKNOWN_TASK_STATUS));

        verify(taskService, times(1)).findByStatus(any());
    }

    @Test
    void test_getAll() throws Exception {
        when(taskService.findAllForLoggedUser()).thenReturn(Collections.singletonList(dummyEntity()));

        mockMvc.perform(
                        get("/tasks")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(taskService, times(1)).findAllForLoggedUser();
    }

    @Test
    void test_getAll_notFound() throws Exception {
        when(taskService.findAllForLoggedUser()).thenReturn(Collections.emptyList());

        mockMvc.perform(
                        get("/tasks")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(taskService, times(1)).findAllForLoggedUser();
    }

    @Test
    void test_createTask() throws Exception {
        when(taskService.create(any())).thenReturn(dummyEntity());

        mockMvc.perform(
                        post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(dummyDTO()))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty());

        verify(taskService, times(1)).create(any());
    }

    @Test
    void test_updateTask() throws Exception {
        Task task = dummyEntity();
        UpdateTaskRequestDTO requestDTO = new UpdateTaskRequestDTO(null, "newDesc");

        when(taskService.update(anyString(), any())).thenReturn(task);

        mockMvc.perform(
                        put("/tasks/{id}", task.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(requestDTO))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty());

        verify(taskService, times(1)).update(anyString(), any());
    }

    @Test
    void test_updateTask_notFound() throws Exception {
        when(taskService.update(anyString(), any())).thenThrow(TaskNotFoundException.class);

        mockMvc.perform(
                        put("/tasks/{id}", "test")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(null))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(ErrorCodes.TASK_NOT_FOUND));
    }

    @Test
    void test_deleteTask() throws Exception {
        mockMvc.perform(
                        delete("/tasks/{id}", dummyDTO().getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(Boolean.TRUE));

        verify(taskService, times(1)).deleteById(anyString());
    }

    @Test
    void test_changeStatus() throws Exception {
        when(taskService.changeStatus(anyString(), anyString())).thenReturn(dummyEntity());

        mockMvc.perform(
                put("/tasks/{id}/{status}", dummyDTO().getId(), dummyDTO().getStatus())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty());

        verify(taskService, times(1)).changeStatus(anyString(), any());
    }

    @Test
    void test_changeStatus_taskNotFound() throws Exception {
        when(taskService.changeStatus(anyString(), anyString())).thenThrow(TaskNotFoundException.class);

        mockMvc.perform(
                put("/tasks/{id}/{status}", dummyDTO().getId(), dummyDTO().getStatus())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(ErrorCodes.TASK_NOT_FOUND));
    }

    @Test
    void test_changeStatus_unknownStatus() throws Exception {
        when(taskService.changeStatus(anyString(), anyString())).thenThrow(UnknownTaskStatusException.class);

        mockMvc.perform(
                put("/tasks/{id}/{status}", dummyDTO().getId(), dummyDTO().getStatus())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(ErrorCodes.UNKNOWN_TASK_STATUS));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public TaskMapper taskMapper() {
            return Mappers.getMapper(TaskMapper.class);
        }
    }

    private Task dummyEntity() {
        Task task = new Task();
        task.setId("12345");
        task.setTitle("title");
        task.setDescription("description");
        task.setStatus(TaskStatus.TO_DO);
        task.setUser(new User());

        return task;
    }

    private TaskDTO dummyDTO() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId("12345");
        taskDTO.setTitle("title");
        taskDTO.setDescription("description");
        taskDTO.setStatus(TaskStatus.DONE.getName());

        return taskDTO;
    }

}

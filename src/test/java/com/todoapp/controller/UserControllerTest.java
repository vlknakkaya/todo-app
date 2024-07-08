package com.todoapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoapp.exception.ErrorCodes;
import com.todoapp.exception.types.UserNotFoundException;
import com.todoapp.model.dto.UserDTO;
import com.todoapp.model.entity.User;
import com.todoapp.model.mapper.UserMapper;
import com.todoapp.service.UserService;
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

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Test
    void test_getById() throws Exception {
        User user = dummyEntity();

        when(userService.getById(user.getId())).thenReturn(user);

        mockMvc.perform(
                get("/users/{id}", user.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(user.getId()));

        verify(userService, times(1)).getById(user.getId());
    }

    @Test
    void test_getById_notFound() throws Exception {
        when(userService.getById(any())).thenThrow(UserNotFoundException.class);

        mockMvc.perform(
                get("/users/{id}", "123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(ErrorCodes.USER_NOT_FOUND));
    }

    @Test
    void test_getByUsername() throws Exception {
        User user = dummyEntity();

        when(userService.findByUsernameIgnoreCase(user.getUsername())).thenReturn(user);

        mockMvc.perform(
                get("/users/username/{username}", user.getUsername())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(user.getUsername()));

        verify(userService, times(1)).findByUsernameIgnoreCase(user.getUsername());
    }

    @Test
    void test_getByUsername_notFound() throws Exception {
        when(userService.findByUsernameIgnoreCase(any())).thenThrow(UserNotFoundException.class);

        mockMvc.perform(
                get("/users/username/{username}", "test")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(ErrorCodes.USER_NOT_FOUND));
    }

    @Test
    void test_searchUsers() throws Exception {
        when(userService.searchUser(any(), any())).thenReturn(Collections.singletonList(dummyEntity()));

        mockMvc.perform(
                get("/users/search")
                        .param("firstName", "firstName")
                        .param("lastName", "lastName")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.*.firstName").value("firstName"))
                .andExpect(jsonPath("$.*.lastName").value("lastName"));

        verify(userService, times(1)).searchUser(any(), any());
    }

    @Test
    void test_searchUsers_notFound() throws Exception {
        when(userService.searchUser(any(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(
                get("/users/search")
                        .param("firstName", "firstName")
                        .param("lastName", "lastName")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(userService, times(1)).searchUser(any(), any());
    }

    @Test
    void test_createUser() throws Exception {
        when(userService.create(any())).thenReturn(dummyEntity());

        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dummyDTO()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty());

        verify(userService, times(1)).create(any());
    }

    @Test
    void test_updateUser() throws Exception {
        User user = dummyEntity();

        when(userService.update(anyString(), any())).thenReturn(user);

        mockMvc.perform(
                put("/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dummyDTO()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty());

        verify(userService, times(1)).update(anyString(), any());
    }

    @Test
    void test_updateUser_notFound() throws Exception {
        when(userService.update(anyString(), any())).thenThrow(UserNotFoundException.class);

        mockMvc.perform(
                put("/users/{id}", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dummyDTO()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(ErrorCodes.USER_NOT_FOUND));
    }

    @Test
    void test_deleteUser() throws Exception {
        mockMvc.perform(
                delete("/users/{id}", dummyDTO().getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(Boolean.TRUE));

        verify(userService, times(1)).deleteById(anyString());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserMapper userMapper() {
            return Mappers.getMapper(UserMapper.class);
        }
    }

    private User dummyEntity() {
        User user = new User();
        user.setId("12345");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setUsername("username");
        user.setPassword("password");

        return user;
    }

    private UserDTO dummyDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId("12345");
        userDTO.setFirstName("firstName");
        userDTO.setLastName("lastName");
        userDTO.setUsername("username");
        userDTO.setPassword("password");

        return userDTO;
    }

}

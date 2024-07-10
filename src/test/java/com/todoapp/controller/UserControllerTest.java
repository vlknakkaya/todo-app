package com.todoapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoapp.exception.ErrorCodes;
import com.todoapp.exception.types.UserNotFoundException;
import com.todoapp.model.dto.ChangePasswordRequestDTO;
import com.todoapp.model.dto.UpdateUserRequestDTO;
import com.todoapp.model.entity.User;
import com.todoapp.model.mapper.UserMapper;
import com.todoapp.service.UserService;
import com.todoapp.service.auth.JwtService;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @MockBean
    private JwtService jwtService;

    @Test
    void test_getUser() throws Exception {
        when(userService.getLoggedUser()).thenReturn(dummyEntity());

        mockMvc.perform(get("/users/profile")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty());

        verify(userService, times(1)).getLoggedUser();
    }

    @Test
    void test_updateUser() throws Exception {
        User user = dummyEntity();
        UpdateUserRequestDTO requestDTO = new UpdateUserRequestDTO(null, "test", null);

        when(userService.update(any())).thenReturn(user);

        mockMvc.perform(
                put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty());

        verify(userService, times(1)).update(any());
    }

    @Test
    void test_updateUser_notFound() throws Exception {
        when(userService.update(any())).thenThrow(UserNotFoundException.class);

        mockMvc.perform(
                put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(null))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(ErrorCodes.USER_NOT_FOUND));
    }

    @Test
    void test_deleteUser() throws Exception {
        mockMvc.perform(
                delete("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(Boolean.TRUE));

        verify(userService, times(1)).deleteUser();
    }

    @Test
    void test_changePassword() throws Exception {
        ChangePasswordRequestDTO requestDTO = new ChangePasswordRequestDTO("test", "test");
        when(userService.changePassword(any(), any())).thenReturn(Boolean.TRUE);

        mockMvc.perform(put("/users/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(Boolean.TRUE));

        verify(userService, times(1)).changePassword(any(), any());
    }

    @Test
    void test_changePassword_badCredentials() throws Exception {
        when(userService.changePassword(any(), any())).thenThrow(BadCredentialsException.class);

        mockMvc.perform(put("/users/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(null))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(ErrorCodes.AUTHENTICATION_FAILED));

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

}

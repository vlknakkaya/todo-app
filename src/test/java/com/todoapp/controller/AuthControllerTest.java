package com.todoapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoapp.model.dto.LoginRequestDTO;
import com.todoapp.model.dto.LoginResponseDTO;
import com.todoapp.model.dto.SignupRequestDTO;
import com.todoapp.model.entity.User;
import com.todoapp.model.mapper.UserMapper;
import com.todoapp.service.auth.AuthService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Mock
    private UserMapper userMapper;

    @MockBean
    private JwtService jwtService;

    @Test
    @WithMockUser("spring")
    void test_login() throws Exception {
        LoginRequestDTO requestDTO = getLoginRequestDTO();
        LoginResponseDTO responseDTO = getLoginResponseDTO();

        when(authService.login(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(requestDTO.getUsername()))
                .andExpect(jsonPath("$.token").isNotEmpty());

        verify(authService, times(1)).login(any());
    }

    @Test
    @WithMockUser("spring")
    void test_signup() throws Exception {
        SignupRequestDTO requestDTO = getSignupRequestDTO();
        User user = getDummyUser();

        when(authService.signup(any())).thenReturn(user);

        mockMvc.perform(post("/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").value(requestDTO.getUsername()))
                .andExpect(jsonPath("$.firstName").value(requestDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(requestDTO.getLastName()));

        verify(authService, times(1)).signup(any());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserMapper userMapper() {
            return Mappers.getMapper(UserMapper.class);
        }
    }

    private LoginRequestDTO getLoginRequestDTO() {
        return new LoginRequestDTO("username", "password");
    }

    private LoginResponseDTO getLoginResponseDTO() {
        return new LoginResponseDTO("username", "token");
    }

    private SignupRequestDTO getSignupRequestDTO() {
        return new SignupRequestDTO("username", "firstName", "lastName", "password");
    }

    private User getDummyUser() {
        User user = new User();
        user.setId("12345");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setUsername("username");
        user.setPassword("password");

        return user;
    }

}

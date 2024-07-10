package com.todoapp.service.auth;

import com.todoapp.model.dto.LoginRequestDTO;
import com.todoapp.model.dto.SignupRequestDTO;
import com.todoapp.model.entity.User;
import com.todoapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    // TODO: incorrect result
    @Test
    void test_login() {
        LoginRequestDTO requestDTO = new LoginRequestDTO("username", "password");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(dummyUser()));

        authService.login(requestDTO);

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(any());
    }

    @Test
    void test_signup() {
        SignupRequestDTO requestDTO = new SignupRequestDTO("username", "firstName",
                "lastName", "password");

        authService.signup(requestDTO);

        verify(userRepository, times(1)).save(any());
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

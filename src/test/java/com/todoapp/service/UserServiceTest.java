package com.todoapp.service;

import com.todoapp.model.dto.ChangePasswordRequestDTO;
import com.todoapp.model.dto.UpdateUserRequestDTO;
import com.todoapp.model.entity.User;
import com.todoapp.repository.UserRepository;
import com.todoapp.service.auth.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void test_getLoggedUser() {
        userService.getLoggedUser();

        verify(authService, times(1)).getLoggedUser();
    }

    @Test
    void test_update() {
        User user = dummyUser();
        UpdateUserRequestDTO updateUserRequestDTO = new UpdateUserRequestDTO(null, "test", "test");

        when(authService.getLoggedUser()).thenReturn(user);

        userService.update(updateUserRequestDTO);

        verify(authService, times(1)).getLoggedUser();
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void test_deleteUser() {
        userService.deleteUser();

        verify(authService, times(1)).getLoggedUser();
        verify(userRepository, times(1)).delete(any());
    }

    @Test
    void test_changePassword() {
        User user = dummyUser();
        ChangePasswordRequestDTO changePasswordRequestDTO = new ChangePasswordRequestDTO(user.getPassword(), "test");

        when(passwordEncoder.encode(any())).thenReturn(user.getPassword());
        when(authService.getLoggedUser()).thenReturn(user);

        userService.changePassword(changePasswordRequestDTO.getOldPassword(), changePasswordRequestDTO.getNewPassword());

        verify(authService, times(1)).getLoggedUser();
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

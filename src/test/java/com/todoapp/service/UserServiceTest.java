package com.todoapp.service;

import com.todoapp.exception.types.UserNotFoundException;
import com.todoapp.model.entity.User;
import com.todoapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void test_findByUsernameIgnoreCase() {
        String username = "username";

        when(userRepository.findByUsernameIgnoreCase(username)).thenReturn(Optional.of(dummyUser()));

        userService.findByUsernameIgnoreCase(username);

        verify(userRepository, times(1)).findByUsernameIgnoreCase(username);
    }

    @Test
    void test_findByUsernameIgnoreCase_notFound() {
        assertThrows(UserNotFoundException.class, () -> userService.findByUsernameIgnoreCase("username"));
    }

    @Test
    void test_searchUser() {
        when(userRepository.searchUser(any(), any())).thenReturn(Collections.singletonList(dummyUser()));

        userService.searchUser(any(), any());

        verify(userRepository, times(1)).searchUser(any(), any());
    }

    @Test
    void test_getById() {
        when(userRepository.findById(any())).thenReturn(Optional.of(dummyUser()));

        userService.getById(any());

        verify(userRepository, times(1)).findById(any());
    }

    @Test
    void test_getById_notFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getById("12345"));
    }

    @Test
    void test_create() {
        userService.create(dummyUser());

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void test_update() {
        when(userRepository.findById(any())).thenReturn(Optional.of(dummyUser()));

        userService.update("123456", dummyUser());

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void test_update_notFound() {
        assertThrows(UserNotFoundException.class, () -> userService.update("12345", dummyUser()));
    }

    @Test
    void test_deleteById() {
        String id = "12345";

        userService.deleteById(id);

        verify(userRepository, times(1)).deleteById(id);
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

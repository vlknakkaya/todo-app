package com.todoapp.service;

import com.todoapp.exception.types.UserNotFoundException;
import com.todoapp.model.dto.UpdateUserRequestDTO;
import com.todoapp.model.entity.User;
import com.todoapp.repository.UserRepository;
import com.todoapp.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthService authService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    public User getLoggedUser() {
        return authService.getLoggedUser();
    }

    public User update(UpdateUserRequestDTO requestDTO) throws UserNotFoundException {
        User user = authService.getLoggedUser();

        if (requestDTO.getUsername() != null) {
            user.setUsername(user.getUsername());
        }
        if (requestDTO.getFirstName() != null) {
            user.setFirstName(user.getFirstName());
        }
        if (requestDTO.getLastName() != null) {
            user.setLastName(user.getLastName());
        }

        return userRepository.save(user);
    }

    public void deleteUser() {
        userRepository.delete(authService.getLoggedUser());
    }

    public Boolean changePassword(String oldPassword, String newPassword) {
        User user = authService.getLoggedUser();
        if (!user.getPassword().equals(passwordEncoder.encode(oldPassword))) {
            throw new BadCredentialsException("Wrong password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return Boolean.TRUE;
    }

}

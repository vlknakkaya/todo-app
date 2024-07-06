package com.todoapp.service;

import com.todoapp.exception.types.UserNotFoundException;
import com.todoapp.model.entity.User;
import com.todoapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsernameIgnoreCase(String username) throws UserNotFoundException {
        return userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException("email", username));
    }

    public List<User> searchUser(String firstName, String lastName) {
        return userRepository.searchUser(firstName, lastName);
    }

    public User getById(String id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("id", id));
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User update(String id, User user) throws UserNotFoundException {
        User foundedUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("id", id));

        if (user.getFirstName() != null) {
            foundedUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            foundedUser.setLastName(user.getLastName());
        }
        if (user.getUsername() != null) {
            foundedUser.setUsername(user.getUsername());
        }
        if (user.getPassword() != null) {
            foundedUser.setPassword(user.getPassword());
        }

        return userRepository.save(foundedUser);
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

}

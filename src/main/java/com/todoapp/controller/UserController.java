package com.todoapp.controller;

import com.todoapp.model.dto.UserDTO;
import com.todoapp.model.entity.User;
import com.todoapp.model.mapper.UserMapper;
import com.todoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable String id) {
        User user = userService.getById(id);

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getByUsername(@PathVariable String username) {
        User foundedUser = userService.findByUsernameIgnoreCase(username);

        return ResponseEntity.ok(userMapper.toDto(foundedUser));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String firstName, @RequestParam String lastName) {
        List<User> foundedEntities = userService.searchUser(firstName, lastName);

        return ResponseEntity.ok(userMapper.toDtos(foundedEntities));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        User user = userService.create(userMapper.toEntity(userDTO));

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        User user = userService.update(id, userMapper.toEntity(userDTO));

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable String id) {
        userService.deleteById(id);

        return ResponseEntity.ok(Boolean.TRUE);
    }

}

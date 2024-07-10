package com.todoapp.controller;

import com.todoapp.model.dto.ChangePasswordRequestDTO;
import com.todoapp.model.dto.UpdateUserRequestDTO;
import com.todoapp.model.dto.UserDTO;
import com.todoapp.model.entity.User;
import com.todoapp.model.mapper.UserMapper;
import com.todoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUser() {
        User user = userService.getLoggedUser();

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody UpdateUserRequestDTO requestDTO) {
        User user = userService.update(requestDTO);

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteUser() {
        userService.deleteUser();

        return ResponseEntity.ok(Boolean.TRUE);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangePasswordRequestDTO requestDTO) {
        return ResponseEntity.ok(userService.changePassword(requestDTO.getOldPassword(), requestDTO.getNewPassword()));
    }

}

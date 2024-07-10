package com.todoapp.controller;

import com.todoapp.model.dto.LoginRequestDTO;
import com.todoapp.model.dto.LoginResponseDTO;
import com.todoapp.model.dto.SignupRequestDTO;
import com.todoapp.model.dto.UserDTO;
import com.todoapp.model.entity.User;
import com.todoapp.model.mapper.UserMapper;
import com.todoapp.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @Autowired
    public AuthController(AuthService authService, UserMapper userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody SignupRequestDTO signupRequest) {
        User user = authService.signup(signupRequest);

        return ResponseEntity.ok(userMapper.toDto(user));
    }

}

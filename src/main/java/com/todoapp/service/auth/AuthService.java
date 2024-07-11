package com.todoapp.service.auth;

import com.todoapp.exception.types.UserNotFoundException;
import com.todoapp.model.dto.LoginRequestDTO;
import com.todoapp.model.dto.LoginResponseDTO;
import com.todoapp.model.dto.SignupRequestDTO;
import com.todoapp.model.entity.User;
import com.todoapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtService.generateToken(getLoggedUser());

        return new LoginResponseDTO(loginRequestDTO.getUsername(), token);
    }

    public User signup(SignupRequestDTO signupRequestDTO) {
        User user = new User();
        user.setUsername(signupRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
        user.setFirstName(signupRequestDTO.getFirstName());
        user.setLastName(signupRequestDTO.getLastName());

        return userRepository.save(user);
    }

    public User getLoggedUser() {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new UserNotFoundException("username", username));
    }

}

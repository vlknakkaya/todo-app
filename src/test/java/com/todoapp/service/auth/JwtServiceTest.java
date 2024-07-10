package com.todoapp.service.auth;

import com.todoapp.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(jwtService, "secretKey", "dummy");
        ReflectionTestUtils.setField(jwtService, "expiration", 3600000L);
    }

    @Test
    void test_generateToken() {
        String token = jwtService.generateToken(dummyUser());

        Assertions.assertNotNull(token);
    }

    @Test
    void test_resolveToken() {
        String token = "xyz";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        String response = jwtService.resolveToken(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(token, response);
    }

    @Test
    void test_resultToken_null() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        String response = jwtService.resolveToken(request);

        Assertions.assertNull(response);
    }

    @Test
    void test_getUsernameFromToken() {
        String token = jwtService.generateToken(dummyUser());

        String result = jwtService.getUsernameFromToken(token);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(dummyUser().getUsername(), result);
    }

    @Test
    void test_isTokenExpired() {
        String token = jwtService.generateToken(dummyUser());

        boolean result = jwtService.isTokenExpired(token);

        Assertions.assertFalse(result);
    }

    @Test
    void test_isTokenValid() {
        User dummyUser = dummyUser();
        String token = jwtService.generateToken(dummyUser);

        boolean result = jwtService.isTokenValid(token, org.springframework.security.core.userdetails.User.builder()
                .username(dummyUser.getUsername())
                .password(dummyUser.getPassword())
                .build());

        Assertions.assertTrue(result);
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

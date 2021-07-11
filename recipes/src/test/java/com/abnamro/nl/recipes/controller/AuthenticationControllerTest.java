package com.abnamro.nl.recipes.controller;

import com.abnamro.nl.recipes.model.entity.Recipe;
import com.abnamro.nl.recipes.payload.request.LoginRequest;
import com.abnamro.nl.recipes.payload.request.SignUpRequest;
import com.abnamro.nl.recipes.payload.response.JwtResponse;
import com.abnamro.nl.recipes.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService authenticationService;

    @Test
    public void givenSignUpRequest_whenRegisterUser_thenStatus200WithSuccessfulMessage(){
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("Abc");
        signUpRequest.setEmail("Abc@xyz.com");
        signUpRequest.setPassword("psw123");
        Set<String> roles = new HashSet<>();
        roles.add("user");
        roles.add("admin");
        signUpRequest.setRole(roles);
        String expectedMessage = "User registered successfully!";
        ResponseEntity responseEntity = new ResponseEntity<>(expectedMessage, HttpStatus.OK);


        when(authenticationService.signUp(any(SignUpRequest.class))).thenReturn(responseEntity);

        ResponseEntity response = authenticationController.registerUser(signUpRequest);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo("User registered successfully!");
    }

    @Test
    public void givenSigninRequest_whenLoginRequest_thenStatus200WithSuccessfulMessage(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Abc");
        loginRequest.setPassword("psw123");

        JwtResponse expectedResponse = new JwtResponse("eyJhbGciOiJIUzUxMiJ9.", 4L,"Kashi", "Kashi@xyz.com", Arrays.asList("ROLE_USER","ROLE_ADMIN"));
        ResponseEntity responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);


        when(authenticationService.signIn(any(LoginRequest.class))).thenReturn(responseEntity);

        ResponseEntity response = authenticationController.authenticateUser(loginRequest);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertNotNull(responseEntity.getBody());
    }
}

package com.abnamro.nl.recipes.service;

import com.abnamro.nl.recipes.payload.request.LoginRequest;
import com.abnamro.nl.recipes.payload.request.SignUpRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<?> signIn(LoginRequest loginRequest);

    ResponseEntity<?> signUp(SignUpRequest signUpRequest);


}

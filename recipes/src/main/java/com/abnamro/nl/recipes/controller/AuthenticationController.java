package com.abnamro.nl.recipes.controller;


import com.abnamro.nl.recipes.payload.request.LoginRequest;
import com.abnamro.nl.recipes.payload.request.SignUpRequest;
import com.abnamro.nl.recipes.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * AuthenticationController is authenticate end users.
 * In order to access recipe end points users must signup.
 * And sufficient roles to access the end points.
 *  AuthenticationController is REST API and have below endpoints
 *  <p>signin</p>
 *  <p>signup</p>
 */

@RestController
@RequestMapping("/v1/api/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Constructor injection of authenticationService
     * @param authenticationService
     */
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /*public void AuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }*/


    /**
     * Authenticate users with credentials
     * @param loginRequest of login users name and password
     * @return JwtResponse
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.signIn(loginRequest);
    }

    /**
     * Register user with signUpRequest form.
     * @param signUpRequest of sign up users details
     * @return messageResponse
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authenticationService.signUp(signUpRequest);
    }
}

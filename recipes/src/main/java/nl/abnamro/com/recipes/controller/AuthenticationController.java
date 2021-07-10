package nl.abnamro.com.recipes.controller;


import nl.abnamro.com.recipes.payload.request.LoginRequest;
import nl.abnamro.com.recipes.payload.request.SignUpRequest;
import nl.abnamro.com.recipes.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    /**
     * Constructor injection of authenticationService
     * @param authenticationService
     */
    @Autowired
    public void AuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


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

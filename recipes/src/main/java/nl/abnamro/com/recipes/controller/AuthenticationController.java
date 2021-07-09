package nl.abnamro.com.recipes.controller;


import nl.abnamro.com.recipes.payload.request.LoginRequest;
import nl.abnamro.com.recipes.payload.request.SignUpRequest;
import nl.abnamro.com.recipes.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    /**
     *
     * @param authenticationService
     */
    @Autowired
    public void AuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    /**
     *
     * @param loginRequest
     * @return
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.signIn(loginRequest);
    }

    /**
     *
     * @param signUpRequest
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authenticationService.signUp(signUpRequest);
    }
}

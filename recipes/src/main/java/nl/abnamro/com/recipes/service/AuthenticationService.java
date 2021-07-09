package nl.abnamro.com.recipes.service;

import nl.abnamro.com.recipes.payload.request.LoginRequest;
import nl.abnamro.com.recipes.payload.request.SignUpRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<?> signIn(LoginRequest loginRequest);

    ResponseEntity<?> signUp(SignUpRequest signUpRequest);


}

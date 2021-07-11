package com.abnamro.nl.recipes.service;

import com.abnamro.nl.recipes.repository.RoleRepository;
import com.abnamro.nl.recipes.repository.UserRepository;
import com.abnamro.nl.recipes.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import com.abnamro.nl.recipes.exception.AuthenticationException;
import com.abnamro.nl.recipes.model.auth.ERole;
import com.abnamro.nl.recipes.model.auth.Role;
import com.abnamro.nl.recipes.model.auth.User;
import com.abnamro.nl.recipes.payload.request.LoginRequest;
import com.abnamro.nl.recipes.payload.request.SignUpRequest;
import com.abnamro.nl.recipes.payload.response.JwtResponse;
import com.abnamro.nl.recipes.payload.response.MessageResponse;
import com.abnamro.nl.recipes.service.auth.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * AuthenticationServiceImpl is have authentication logic of
 * <p>Authorization</p>
 * <p>Authentication</p>
 */
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Signing in registered users with accessToken and user details.
     * @param loginRequest of authorized user
     * @return JwtResponse of token details
     */
    @Override
    public ResponseEntity<?> signIn(LoginRequest loginRequest) {

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwt = jwtUtils.generateJwtToken(authentication);

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var roles = userDetails.getAuthorities().stream()
                                                        .map(item -> item.getAuthority())
                                                        .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                                            userDetails.getId(),
                                            userDetails.getUsername(),
                                            userDetails.getEmail(),
                                            roles));

    }

    /**
     * Authorizing user inorder to access recipe endpoint
     * @param signUpRequest of new user
     * @return messageResponse
     */
    @Override
    public ResponseEntity<?> signUp(SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error_CODE-0001: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error_CODE-0002: Email is already in use!"));
        }

        // Create new user's account
        var user = new User(signUpRequest.getUsername(),
                                                        signUpRequest.getEmail(),
                                                        encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new AuthenticationException("Error_CODE-0003: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                                       .orElseThrow(() -> new AuthenticationException("Error_CODE-0004: Role Admin is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                                     .orElseThrow(() -> new AuthenticationException("Error_CODE-0005: Role mod is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                                      .orElseThrow(() -> new AuthenticationException("Error_CODE-0006: Role User is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}

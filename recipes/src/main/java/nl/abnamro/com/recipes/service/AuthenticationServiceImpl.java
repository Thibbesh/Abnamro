package nl.abnamro.com.recipes.service;

import lombok.extern.slf4j.Slf4j;
import nl.abnamro.com.recipes.model.auth.ERole;
import nl.abnamro.com.recipes.model.auth.Role;
import nl.abnamro.com.recipes.model.auth.User;
import nl.abnamro.com.recipes.payload.request.LoginRequest;
import nl.abnamro.com.recipes.payload.request.SignUpRequest;
import nl.abnamro.com.recipes.payload.response.JwtResponse;
import nl.abnamro.com.recipes.payload.response.MessageResponse;
import nl.abnamro.com.recipes.repository.RoleRepository;
import nl.abnamro.com.recipes.repository.UserRepository;
import nl.abnamro.com.recipes.security.jwt.JwtUtils;
import nl.abnamro.com.recipes.service.auth.UserDetailsImpl;
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
     *
     * @param loginRequest
     * @return
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
     *
     * @param signUpRequest
     * @return
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
                    .orElseThrow(() -> new RuntimeException("Error_CODE-0003: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                                       .orElseThrow(() -> new RuntimeException("Error_CODE-0004: Role Admin is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                                     .orElseThrow(() -> new RuntimeException("Error_CODE-0005: Role mod is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                                      .orElseThrow(() -> new RuntimeException("Error_CODE-0006: Role User is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}

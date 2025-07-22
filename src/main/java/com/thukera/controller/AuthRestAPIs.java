package com.thukera.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import com.thukera.model.forms.LoginForm;
import com.thukera.model.messages.InactiveException;
import com.thukera.model.forms.SignUpForm;
import com.thukera.model.messages.JwtResponse;
import com.thukera.model.entities.Role;
import com.thukera.model.enums.RoleName;
import com.thukera.model.entities.User;

import com.thukera.repository.RoleRepository;
import com.thukera.repository.UserRepository;
import com.thukera.security.jwt.JwtProvider;

import jakarta.validation.Valid;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {

	private static final Logger logger = LogManager.getLogger(AuthRestAPIs.class);
    
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        Optional<User> usuario;

        if(loginRequest.getUsername().contains("@")) {
            usuario = userRepository.findFirstByEmail(loginRequest.getUsername());
        }
        else {
            usuario = userRepository.findByUsername(loginRequest.getUsername());
        }

        if(usuario.get().getStatus().equals(true)) {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        usuario.get().getUsername(),
                        loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtProvider.generateJwtToken(authentication);
            JwtResponse token = new JwtResponse(jwt);

            usuario.get().setToken(token);
            usuario.get().setPassword("");

            return ResponseEntity.ok(usuario);
        }
        else {
            throw new InactiveException("Usuário inativo, contacte seu administrador!");
        }
    }

    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<String>("Fail -> Username está em uso!",
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<String>("Fail -> Email está em uso!",
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByCpf(signUpRequest.getCpf())) {
            return new ResponseEntity<String>("Fail -> CPF está em uso!!",
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getCpf(), signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getAgentId(), signUpRequest.getStatus());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch(role) {
                case "admin":
                    Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Fail! -> Cause: ADMIN Role não encontrada."));
                    roles.add(adminRole);

                    break;
                case "user":
                    Role pmRole = roleRepository.findByName(RoleName.ROLE_PM)
                    .orElseThrow(() -> new RuntimeException("Fail! -> Cause: PM Role não encontrada."));
                    roles.add(pmRole);

                default:
                    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role não encontrada."));
                    roles.add(userRole);
            }
        });

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
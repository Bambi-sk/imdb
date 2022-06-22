package com.javaproject.imbd.controller;

import com.javaproject.imbd.entities.Roles;
import com.javaproject.imbd.entities.Users;
import com.javaproject.imbd.jwt.JwtUtils;
import com.javaproject.imbd.payload.request.LoginRequest;
import com.javaproject.imbd.payload.request.SignupRequest;
import com.javaproject.imbd.payload.response.JwtResponse;
import com.javaproject.imbd.payload.response.MessageResponse;
import com.javaproject.imbd.repositories.RolesRepository;
import com.javaproject.imbd.repositories.UsersRepository;
import com.javaproject.imbd.services.impl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsersRepository userRepository;

    @Autowired
    RolesRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        List <Roles> roless=new ArrayList<>();
        Roles role= new Roles();
        for (String r: roles){
            if(roleRepository.findByRole(r).isPresent()){
                role=roleRepository.findByRole(r).orElseThrow(() -> new UsernameNotFoundException("User Not Found with roles: "));
                roless.add(role);
            }

        }
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getFullname(),
                userDetails.getEmail(),
                roless));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {


        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Users user = new Users();
        user.setEmail(signUpRequest.getEmail());
        user.setFullName(signUpRequest.getFullname());
        user.setPassword( passwordEncoder.encode(signUpRequest.getPassword()));
        List<Roles> strRoles = signUpRequest.getRole();
        List<Roles> roles = new ArrayList<>();

        if (strRoles == null) {
            Roles userRole = roleRepository.findByRole("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.getRole()) {
                    case "ROLE_ADMIN":
                        Roles adminRole = roleRepository.findByRole("ROLE_ADMIN")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "ROLE_MODERATOR":
                        Roles modRole = roleRepository.findByRole("ROLE_MODERATOR")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Roles userRole = roleRepository.findByRole("ROLE_USER")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}

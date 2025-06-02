package com.exam.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication; // ✅ Correct import
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exam.dto.request.LoginRequest;
import com.exam.dto.request.RegisterRequest;
import com.exam.dto.response.JwtResponse;
import com.exam.dto.response.MessageResponse;
import com.exam.model.User;
import com.exam.repsoitory.RoleRepository;
import com.exam.repsoitory.UserRepository;
import com.exam.security.jwt.JwtUtils;
import com.exam.security.service.UserDetailsImpl;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public AuthService(AuthenticationManager authenticationManager, 
                      UserRepository userRepository, 
                      RoleRepository roleRepository, 
                      PasswordEncoder encoder, 
                      JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal(); 
       // This line bracked  (UserDetailsImpl):

        	//“Get the currently logged-in user’s details from the
        	//authentication object, and treat it as a UserDetailsImpl object (our custom user class).
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return new JwtResponse(jwt);
    }
    public MessageResponse registerUser(RegisterRequest registerRequest) {
        // Check for duplicate email
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new MessageResponse("Error: Email is already in use!");
        }

        // Check for duplicate phone
        if (userRepository.existsByPhone(registerRequest.getPhone())) {
            return new MessageResponse("Error: Phone number is already in use!");
        }

        // Create new User instance
        User user = new User(
            registerRequest.getUsername(),
            registerRequest.getEmail(),
            registerRequest.getFirstname(),
            registerRequest.getLastname(),
            registerRequest.getProfileImage(),
            registerRequest.getPhone(),
            encoder.encode(registerRequest.getPassword()) // Encrypt password
        );

        // Save user to database
        userRepository.save(user);

        return new MessageResponse("User registered successfully!");
    }
}
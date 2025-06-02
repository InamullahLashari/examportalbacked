package com.exam.controller;


import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exam.dto.request.LoginRequest;
import com.exam.dto.request.RegisterRequest;
import com.exam.dto.response.JwtResponse;
import com.exam.dto.response.MessageResponse;
import com.exam.model.Role;
import com.exam.model.User;
import com.exam.service.AuthService;
import com.exam.service.UserService;


import jakarta.validation.Valid;

import com.exam.model.UserRole;

@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:4200")  // ✅ CORRECT
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthService authService;
    
    
    ////////////////////////////test api/////////////////
    @GetMapping("/text")
    public ResponseEntity<String> getText() {
        // Returning a simple message as plain text
        return ResponseEntity.ok("This is a test text message.");
    }

///////////////////////////////////// this is the user post data///////////////////////////////////////////////////////

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.registerUser(registerRequest));
    }

      
/////////////////////////////////login///////////////
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

///////////////////////////////////////////////////End/////////////////////////////////////////////////////////////////
    
    
    // Get user by username
    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return this.userService.getUser(username);
    }

    
    

    
////////////////////////////this is the user get data by id////////////////////////////
    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Delete user by ID
    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean status = userService.deleteUser(id);

        if (status) {
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID " + id + " not found.");
        }
    }

    // Update user by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUserDetail(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUserDetail(id, user);

        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
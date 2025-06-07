package com.emp.controllers;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.emp.dto.AuthRequest;
import com.emp.dto.AuthResponse;
import com.emp.dto.RegisterRequest;
import com.emp.entities.User;
import com.emp.repository.UserRepository;
import com.emp.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest req) {
    	System.out.println(req);
        if (userRepository.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username already exists"));
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(req.getRole());

        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
    	System.out.println("Called");
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            // Return JSON object with message on failure
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }

        User user = (User) userRepository.findByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "User not found"));
        }

        String token = jwtUtil.generateToken(user);

        // Return JSON object with token on success
        return ResponseEntity.ok(Map.of("token", token));
    }

}

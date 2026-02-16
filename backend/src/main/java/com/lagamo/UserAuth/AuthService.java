package com.lagamo.UserAuth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injected from SecurityConfig

    @Autowired
    private TokenProvider tokenProvider; // Injected from Step 2

    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Usage
        return userRepository.save(user);
    }

    // Updated Login to return a Token
    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Generate and return the token!
        return tokenProvider.generateToken(user);
    }
}
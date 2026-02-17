package com.lagamo.UserAuth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return ResponseEntity.ok(authService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        try {
            // Get the token from Service
            String token = authService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());

            // Return it as a JSON object
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /api/auth/me — return current user's profile from JWT
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String email = tokenProvider.getEmailFromToken(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Map<String, Object> profile = new LinkedHashMap<>();
            profile.put("id", user.getId());
            profile.put("firstname", user.getFirstname());
            profile.put("middlename", user.getMiddlename());
            profile.put("lastname", user.getLastname());
            profile.put("email", user.getEmail());
            profile.put("contactNumber", user.getContactNumber());
            profile.put("street", user.getStreet());
            profile.put("barangay", user.getBarangay());
            profile.put("municipality", user.getMunicipality());
            profile.put("province", user.getProvince());
            profile.put("country", user.getCountry());
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
    }

    // Tiny helper class to make the JSON look like: { "token": "..." }
    // You can put this at the bottom of AuthController.java
    static class AuthResponse {
        private String token;

        public AuthResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }
}
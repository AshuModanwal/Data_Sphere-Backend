package com.datasphere.backend.controllers;

import com.datasphere.backend.dtos.UserDto;
import com.datasphere.backend.dtos.LoginUserDto;
import com.datasphere.backend.models.User;
import com.datasphere.backend.services.AuthenticationService;

import com.datasphere.backend.utils.JwtTokenUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;         // << correct import
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthenticationController(AuthenticationService authenticationService, JwtTokenUtil jwtTokenUtil) {
        this.authenticationService = authenticationService;
        this.jwtTokenUtil =  jwtTokenUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid @RequestBody UserDto userDto,
            BindingResult br                            // << correct type
    ) {
        if (br.hasErrors()) {
            var errors = br.getFieldErrors()
                    .stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();
            return ResponseEntity
                    .badRequest()
                    .body(errors);
        }

        User created = authenticationService.registerNewUser(userDto);
        return ResponseEntity
                .ok("User registered with ID: " + created.getId());
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginUserDto loginUserDto,
            BindingResult br
    ){
        if (br.hasErrors()) {
            var errors = br.getFieldErrors()
                    .stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();
            return ResponseEntity
                    .badRequest()
                    .body(errors);
        }

        try {
            User user = authenticationService.authenticateUser(loginUserDto);

            String token  = jwtTokenUtil.generateJwtToken(user);
            return ResponseEntity.ok().body(
                    Map.of(
                            "message", "Login successful",
                            "token", token
                    )
            );
        }catch (RuntimeException error){
            return ResponseEntity
                    .status(401)
                    .body(Map.of("error", error.getMessage()));
        }
    }

    @GetMapping("/profileData")
    public ResponseEntity<?> getProfileData(
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            // Check for Bearer prefix
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Map.of("error", "Missing or invalid Authorization header"));
            }


            String token = authHeader.substring(7); // remove "Bearer "
            String email = jwtTokenUtil.extractUserEmail(token);

            User user = authenticationService.getProfileData(email);

            if (user == null) {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }

            return ResponseEntity.ok(Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "username", user.getUserName(),
                    "company", user.getCompany()
            ));
        }catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token or session expired"));
        }
    }


}

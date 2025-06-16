package com.datasphere.backend.controllers;

import com.datasphere.backend.dtos.SignupUserDto;
import com.datasphere.backend.models.UserModel;
import com.datasphere.backend.services.AuthenticationService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;         // << correct import
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid @RequestBody SignupUserDto signupUserDto,
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

        UserModel created = authenticationService.registerNewUser(signupUserDto);
        return ResponseEntity
                .ok("User registered with ID: " + created.getId());
    }
}

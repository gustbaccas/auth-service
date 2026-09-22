package io.github.gustbaccas.auth_service.controller;

import io.github.gustbaccas.auth_service.dto.*;
import io.github.gustbaccas.auth_service.entity.PasswordResetToken;
import io.github.gustbaccas.auth_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        RegisterResponse response = service.create(request);

        return ResponseEntity
                .status(201)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = service.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        service.forgotPassword(request);
        return ResponseEntity.ok("If this email exists, password reset instructions have been sent.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request){
        service.resetPassword(request);
        return ResponseEntity.ok("Password has been reset successfully");
    }

}



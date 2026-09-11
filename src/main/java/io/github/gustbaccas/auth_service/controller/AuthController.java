package io.github.gustbaccas.auth_service.controller;

import io.github.gustbaccas.auth_service.dto.LoginRequest;
import io.github.gustbaccas.auth_service.dto.LoginResponse;
import io.github.gustbaccas.auth_service.dto.RegisterRequest;
import io.github.gustbaccas.auth_service.dto.RegisterResponse;
import io.github.gustbaccas.auth_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request){
        LoginResponse response = service.login(request);

        return ResponseEntity.ok(response);
    }

}


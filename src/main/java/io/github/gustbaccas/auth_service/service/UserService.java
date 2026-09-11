package io.github.gustbaccas.auth_service.service;

import io.github.gustbaccas.auth_service.dto.LoginRequest;
import io.github.gustbaccas.auth_service.dto.LoginResponse;
import io.github.gustbaccas.auth_service.dto.RegisterRequest;
import io.github.gustbaccas.auth_service.dto.RegisterResponse;
import io.github.gustbaccas.auth_service.entity.User;
import io.github.gustbaccas.auth_service.enums.Role;
import io.github.gustbaccas.auth_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public RegisterResponse create(RegisterRequest request) {

        if (repository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(encoder.encode(request.password()));
        user.setRole(Role.USER);

        User savedUser = repository.save(user);

        return new RegisterResponse(savedUser.getId(), savedUser.getEmail());
    }

    public LoginResponse login(LoginRequest request) {

        Optional<User> optionalUser = repository.findByEmail(request.email());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = optionalUser.get();

        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return new LoginResponse(user.getId(), user.getEmail());

    }
}

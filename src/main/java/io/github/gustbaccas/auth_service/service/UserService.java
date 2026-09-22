package io.github.gustbaccas.auth_service.service;

import io.github.gustbaccas.auth_service.dto.*;
import io.github.gustbaccas.auth_service.entity.PasswordResetToken;
import io.github.gustbaccas.auth_service.entity.User;
import io.github.gustbaccas.auth_service.enums.Role;
import io.github.gustbaccas.auth_service.repository.PasswordResetTokenRepository;
import io.github.gustbaccas.auth_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public UserService(UserRepository repository, PasswordEncoder encoder, JwtService jwtService, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.repository = repository;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
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

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(user.getId(), user.getEmail(), token);

    }

    public void forgotPassword(ForgotPasswordRequest request) {
        Optional<User> optionalUser = repository.findByEmail(request.email());

        if (optionalUser.isEmpty()) {
            return;
        }

        User user = optionalUser.get();

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));

        passwordResetTokenRepository.save(resetToken);

        System.out.println(">>> Reset token generated: " + token);
    }

    public void resetPassword(ResetPasswordRequest request) {
        Optional<PasswordResetToken> optionalToken = passwordResetTokenRepository.findByToken(request.token());

        if (optionalToken.isEmpty()) {
            throw new RuntimeException("Invalid or expired token");
        }

        PasswordResetToken resetToken = optionalToken.get();

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invalid or expired token");
        }

        User user = resetToken.getUser();
        user.setPassword(encoder.encode(request.newPassword()));
        repository.save(user);

    }

}

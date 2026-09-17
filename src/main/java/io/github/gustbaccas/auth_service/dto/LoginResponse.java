package io.github.gustbaccas.auth_service.dto;

public record LoginResponse(
        Long id,
        String email,
        String token
) {
}

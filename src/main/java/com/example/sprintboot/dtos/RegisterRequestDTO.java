package com.example.sprintboot.dtos;

import com.example.sprintboot.domain.user.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(@NotBlank String name, @Email String email, @NotBlank String password, UserRole role) {
}

package com.aryan.wallet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(
        @NotBlank String name,
        @NotBlank @Email String email) {}
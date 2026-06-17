package com.odonto.odonto_system.shared.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfConstraintValidator implements ConstraintValidator<Cpf, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true; // Deixe o @NotBlank lidar com valores nulos
        return CpfValidator.isValid(value);
    }
}
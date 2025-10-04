package com.example.user_srv._annotation.impl;

import com.example.user_srv._annotation.NoHtml;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NoHtmlValidator implements ConstraintValidator<NoHtml, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        String defaultPattern = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";

        return !value.matches(defaultPattern);
    }

}

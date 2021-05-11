package com.example.cardsregisters.validators;

import com.example.cardsregisters.dto.Card;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CustomCardValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Card.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "description", "field.required");
        Card request = (Card) o;
        if (request.getDescription() == null && request.getDescription().trim().length() == 0) {
            errors.rejectValue(
                    "description",
                    "field.required");
        }
    }
}

package org.jxiang.validators;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.jxiang.exceptions.ObjectNotValidException;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ObjectValidator<T> {

  private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

  private final Validator validator = factory.getValidator();

  public void validate(final T objectToValidate) {
    final Set<ConstraintViolation<T>> violations = validator.validate(objectToValidate);

    if (!violations.isEmpty()) {
      final Set<String> errorMessages = violations.stream()
          .map(ConstraintViolation::getMessage)
          .collect(Collectors.toSet());
      throw new ObjectNotValidException(errorMessages);
    }

  }

}

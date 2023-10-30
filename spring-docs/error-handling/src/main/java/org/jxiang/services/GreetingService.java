package org.jxiang.services;

import lombok.RequiredArgsConstructor;
import org.jxiang.dto.Greeting;
import org.jxiang.validators.ObjectValidator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GreetingService {

  private final ObjectValidator<Greeting> validator;

  public String saveGreetings(final Greeting greeting) {
    validator.validate(greeting);
    
    return
      greeting.getMessage().toUpperCase() +
        ": " +
        greeting.getFrom().toUpperCase() +
        " -> " +
        greeting.getTo().toUpperCase();
  }

  public String throwException() {
    throw new IllegalStateException("Some exception happened!");
  }

}

package com.techdocs.di.svc.greeting;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary // @Qualifier > @Primary
@Service
public class GreetingSvcPrimaryImpl implements GreetingSvc {

  @Override
  public String sayGreeting() {
    return "Hello World from the base svc - Primary";
  }

}

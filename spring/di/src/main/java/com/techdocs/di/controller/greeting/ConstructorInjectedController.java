package com.techdocs.di.controller.greeting;

import com.techdocs.di.svc.greeting.GreetingSvc;

public class ConstructorInjectedController {

  private final GreetingSvc greetingSvc;

  public ConstructorInjectedController(final GreetingSvc greetingSvc) {
    this.greetingSvc = greetingSvc;
  }

  public String hello() {
    return greetingSvc.sayGreeting();
  }

}

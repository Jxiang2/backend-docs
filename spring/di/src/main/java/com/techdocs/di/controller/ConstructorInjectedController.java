package com.techdocs.di.controller;

import com.techdocs.di.svc.GreetingSvc;

public class ConstructorInjectedController {

  private final GreetingSvc greetingSvc;

  public ConstructorInjectedController(final GreetingSvc greetingSvc) {
    this.greetingSvc = greetingSvc;
  }

  public String hello() {
    return greetingSvc.sayGreeting();
  }

}

package com.techdocs.di.controller.greeting;

import com.techdocs.di.svc.greeting.GreetingSvc;

public class PropertyInjectedController {

  GreetingSvc greetingSvc;

  public String hello() {
    return greetingSvc.sayGreeting();
  }

}

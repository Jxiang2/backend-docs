package com.techdocs.di.controller;

import com.techdocs.di.svc.GreetingSvc;

public class PropertyInjectedController {
  
  GreetingSvc greetingSvc;

  public String hello() {
    return greetingSvc.sayGreeting();
  }

}

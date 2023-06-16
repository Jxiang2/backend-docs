package com.techdocs.di.controller;

import com.techdocs.di.svc.GreetingSvc;
import com.techdocs.di.svc.GreetingSvcImpl;
import org.springframework.stereotype.Controller;

@Controller
public class MyController {

  private final GreetingSvc greetingSvc;

  public MyController() {
    this.greetingSvc = new GreetingSvcImpl();
  }

  public String hello() {
    return greetingSvc.sayGreeting();
  }

}

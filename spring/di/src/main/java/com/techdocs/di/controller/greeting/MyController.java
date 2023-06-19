package com.techdocs.di.controller.greeting;

import com.techdocs.di.svc.greeting.GreetingSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

@Controller
public class MyController {

  private final GreetingSvc greetingSvc;

  @Autowired
  public MyController(@Qualifier("greetingSvcImpl") final GreetingSvc greetingSvc) { // specify non-primary bean
    this.greetingSvc = greetingSvc;
  }

  public String hello() {
    return greetingSvc.sayGreeting();
  }

}

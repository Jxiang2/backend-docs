package com.techdocs.di.controller.greeting;

import com.techdocs.di.svc.greeting.GreetingSvc;
import com.techdocs.di.svc.greeting.GreetingSvcImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

@Controller
public class MyController {

  private final GreetingSvc greetingSvc;

  @Autowired
  public MyController(@Qualifier("greetingSvcImpl") final GreetingSvc greetingSvc) {
    this.greetingSvc = new GreetingSvcImpl();
  }

  public String hello() {
    return greetingSvc.sayGreeting();
  }

}

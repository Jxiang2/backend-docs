package com.techdocs.di.controller.i18n;

import com.techdocs.di.svc.greeting.GreetingSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

@Controller
public class Myi18nController {

  private final GreetingSvc greetingSvc;

  @Autowired
  public Myi18nController(@Qualifier("i18nSvc") final GreetingSvc greetingSvc) {
    this.greetingSvc = greetingSvc;
  }

  public String hello() {
    return greetingSvc.sayGreeting();
  }

}

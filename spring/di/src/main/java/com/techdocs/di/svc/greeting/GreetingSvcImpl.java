package com.techdocs.di.svc.greeting;


import org.springframework.stereotype.Service;

@Service
public class GreetingSvcImpl implements GreetingSvc {

  @Override
  public String sayGreeting() {
    return "Hello World from the base svc";
  }

}

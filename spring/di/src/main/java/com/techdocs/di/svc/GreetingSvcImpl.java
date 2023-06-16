package com.techdocs.di.svc;

public class GreetingSvcImpl implements GreetingSvc {

  @Override
  public String sayGreeting() {
    return "Hello World from the base svc";
  }

}

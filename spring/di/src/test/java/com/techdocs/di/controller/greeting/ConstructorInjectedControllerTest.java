package com.techdocs.di.controller.greeting;

import com.techdocs.di.svc.greeting.GreetingSvcImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Mock constructor injection. Recommended
 */
class ConstructorInjectedControllerTest {

  private ConstructorInjectedController controller;

  @BeforeEach
  void setUp() {
    controller = new ConstructorInjectedController(new GreetingSvcImpl());
  }


  @Test
  void hello() {
    assertEquals("Hello World from the base svc", controller.hello());
  }

}
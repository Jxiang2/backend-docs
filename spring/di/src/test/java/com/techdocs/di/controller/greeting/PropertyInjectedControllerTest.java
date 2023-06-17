package com.techdocs.di.controller.greeting;

import com.techdocs.di.svc.greeting.GreetingSvcImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Mock property injection. Not recommended
 */
class PropertyInjectedControllerTest {

  PropertyInjectedController controller;

  @BeforeEach
  void setUp() {
    controller = new PropertyInjectedController();
    controller.greetingSvc = new GreetingSvcImpl();
  }

  @Test
  void hello() {
    assertEquals("Hello World from the base svc", controller.hello());
  }

}
package com.techdocs.di.controller.greeting;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Recommended way to do dependency injection in Spring is to use constructor injection.
 */
@SpringBootTest
class MyControllerTest {

  private final MyController myController;

  @Autowired
  public MyControllerTest(final MyController myController) {
    this.myController = myController;
  }

  @Test
  void hello() {
    System.out.println(myController.hello());
  }

}
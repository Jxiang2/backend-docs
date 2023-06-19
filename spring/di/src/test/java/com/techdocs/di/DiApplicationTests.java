package com.techdocs.di;

import com.techdocs.di.controller.greeting.MyController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class DiApplicationTests {


  private final MyController controller;

  private final ApplicationContext context;

  @Autowired
  public DiApplicationTests(final MyController controller, final ApplicationContext context) {
    this.controller = controller;
    this.context = context;
  }

  @Test
  void controllerFromCtx() {
    final MyController controller = context.getBean(MyController.class);
    System.out.println(controller.hello());
  }

  @Test
  void controllerFromAutowire() {
    System.out.println(controller.hello());
  }

}

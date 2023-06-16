package com.techdocs.di.controller;

import org.junit.jupiter.api.Test;

class MyControllerTest {

  @Test
  void hello() {
    final MyController myController = new MyController();
    System.out.println(myController.hello());
  }

}
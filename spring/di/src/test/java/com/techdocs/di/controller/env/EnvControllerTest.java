package com.techdocs.di.controller.env;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Assignment: Multi profile
 */
@ActiveProfiles({"qa", "default"}) // either specify all profiles, or do not use this annotation
@SpringBootTest
class EnvControllerTest {

  private final EnvController envController;

  @Autowired
  public EnvControllerTest(final EnvController envController) {
    this.envController = envController;
  }

  @Test
  void getEnv() {
    System.out.println(envController.getEnv());
  }

}
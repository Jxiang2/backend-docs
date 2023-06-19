package com.techdocs.di.controller.i18n;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Demo Profile
 * 2 service beans have the same name, need profile to indicate which one to use
 */

@ActiveProfiles({"ES", "dev"}) // either specify all profiles, or do not use this annotation
@SpringBootTest
public class Myi18nControllerTest {

  final private Myi18nController myi18nController;

  @Autowired
  public Myi18nControllerTest(final Myi18nController myi18nController) {
    this.myi18nController = myi18nController;
  }

  @Test
  void hello() {
    System.out.println(myi18nController.hello());
  }

}

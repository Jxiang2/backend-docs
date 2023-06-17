package com.techdocs.di.controller.env;

import com.techdocs.di.svc.env.EnvSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class EnvController {

  private final EnvSvc envSvc;

  @Autowired
  public EnvController(final EnvSvc envSvc) {
    this.envSvc = envSvc;
  }

  public String getEnv() {
    return "You are in: " + envSvc.getEnv() + " environment";
  }

}

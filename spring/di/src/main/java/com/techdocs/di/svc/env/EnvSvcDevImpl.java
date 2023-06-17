package com.techdocs.di.svc.env;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile({"dev", "default"})
@Service
public class EnvSvcDevImpl implements EnvSvc {

  @Override
  public String getEnv() {
    return "dev";
  }

}

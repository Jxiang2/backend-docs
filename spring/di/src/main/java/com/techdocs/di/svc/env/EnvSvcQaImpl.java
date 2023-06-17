package com.techdocs.di.svc.env;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("qa")
@Service
public class EnvSvcQaImpl implements EnvSvc {

  @Override
  public String getEnv() {
    return "qa";
  }

}

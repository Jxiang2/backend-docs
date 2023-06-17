package com.techdocs.di.svc.i18n;

import com.techdocs.di.svc.greeting.GreetingSvc;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile({"EN", "default"})
@Service("i18nSvc")
public class EnGreetingSvc implements GreetingSvc {

  @Override
  public String sayGreeting() {
    return "Hello World - EN";
  }

}

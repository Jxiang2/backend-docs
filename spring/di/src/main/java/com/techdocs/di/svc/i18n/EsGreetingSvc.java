package com.techdocs.di.svc.i18n;

import com.techdocs.di.svc.greeting.GreetingSvc;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("ES")
@Service("i18nSvc")
public class EsGreetingSvc implements GreetingSvc {

  public String sayGreeting() {
    return "Hola Mundo - ES";
  }

}

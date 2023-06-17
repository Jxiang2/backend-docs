package com.techdocs.di;

import com.techdocs.di.controller.greeting.MyController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class DiApplication {

  public static void main(final String[] args) {
    final ConfigurableApplicationContext ctx = SpringApplication.run(DiApplication.class, args);

    final MyController controller = (MyController) ctx.getBean("myController");
    DiApplication.log.info(controller.hello());
  }

}

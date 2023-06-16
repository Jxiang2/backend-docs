package com.techdocs.di;

import com.techdocs.di.controller.MyController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiApplication {

  public static void main(final String[] args) {
    final ConfigurableApplicationContext ctx = SpringApplication.run(DiApplication.class, args);

    final MyController controller = (MyController) ctx.getBean("myController");
    System.out.println(controller.hello());
  }

}

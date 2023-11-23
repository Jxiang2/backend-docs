package org.jxiang.controllers;

import lombok.RequiredArgsConstructor;
import org.jxiang.dto.Greeting;
import org.jxiang.services.GreetingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/greetings")
@RequiredArgsConstructor
public class GreetingController {

  private final GreetingService service;

  @PostMapping
  public ResponseEntity<String> postGreeting(@RequestBody final Greeting greeting) {
    final String msg = service.saveGreetings(greeting);
    return ResponseEntity.accepted().body(msg);
  }

}

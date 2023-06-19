package com.techdocs.restmvc.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class BeerControllerTest {

  @Autowired
  private BeerController beerController;

  @Test
  void getBeerById() {
    System.out.println(beerController.getBeerById(UUID.randomUUID()));
  }

}
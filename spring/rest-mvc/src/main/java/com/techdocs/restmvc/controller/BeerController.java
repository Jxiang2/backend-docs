package com.techdocs.restmvc.controller;

import com.techdocs.restmvc.pojo.Beer;
import com.techdocs.restmvc.service.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Controller
public class BeerController {

  private final BeerService beerService;

  public Beer getBeerById(final UUID beerId) {
    log.debug("Get beer by id - in controller. id: {}", beerId);
    return beerService.getBeerById(beerId);
  }

}

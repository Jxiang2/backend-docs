package com.techdocs.restmvc.service;

import com.techdocs.restmvc.pojo.Beer;
import com.techdocs.restmvc.pojo.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class BeerServiceImpl implements BeerService {

  @Override
  public Beer getBeerById(final UUID beerId) {
    log.debug("Get beer by id - in service. id: {}", beerId);

    return Beer.builder()
      .id(beerId)
      .version(1)
      .beerName("Galaxy Cat")
      .beerStyle(BeerStyle.PALE_ALE)
      .price(new BigDecimal("12.95"))
      .createdDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now())
      .build();
  }

}

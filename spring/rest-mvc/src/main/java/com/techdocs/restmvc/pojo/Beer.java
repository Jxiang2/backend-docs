package com.techdocs.restmvc.pojo;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@EqualsAndHashCode
@ToString
public class Beer {

  private UUID id;

  private Integer version;

  private String beerName;

  private BeerStyle beerStyle;

  private String upc;

  private Integer quantityOnHand;

  private BigDecimal price;

  private LocalDateTime createdDate;

  private LocalDateTime updateDate;

}

package com.techdocs.restmvc.service;

import com.techdocs.restmvc.pojo.Beer;

import java.util.UUID;

public interface BeerService {

  Beer getBeerById(UUID beerId);

}

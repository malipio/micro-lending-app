package com.github.malipio.micro.lending.app.service;

import org.springframework.data.repository.CrudRepository;

import com.github.malipio.micro.lending.app.domain.Client;

public interface ClientRepository extends CrudRepository<Client, String> {

}

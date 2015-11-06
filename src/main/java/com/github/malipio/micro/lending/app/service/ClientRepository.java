package com.github.malipio.micro.lending.app.service;

import com.github.malipio.micro.lending.app.domain.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, String> {

}

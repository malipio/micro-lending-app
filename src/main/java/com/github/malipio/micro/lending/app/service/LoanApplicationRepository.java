package com.github.malipio.micro.lending.app.service;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import com.github.malipio.micro.lending.app.domain.Client;
import com.github.malipio.micro.lending.app.domain.LoanApplication;

public interface LoanApplicationRepository extends CrudRepository<LoanApplication, Long> {

	public long countForClientPerDay(Client client, LocalDate day);
}

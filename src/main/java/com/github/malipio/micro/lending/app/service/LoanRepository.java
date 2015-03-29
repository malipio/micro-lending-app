package com.github.malipio.micro.lending.app.service;

import org.springframework.data.repository.CrudRepository;

import com.github.malipio.micro.lending.app.domain.Loan;

public interface LoanRepository extends CrudRepository<Loan, Long> {

}

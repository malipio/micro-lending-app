package com.github.malipio.micro.lending.app.service;

import com.github.malipio.micro.lending.app.domain.Loan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LoanRepository extends CrudRepository<Loan, Long> {

	List<Loan> findByLoanApplicationClientPeselOrderByToDesc(String pesel);
}

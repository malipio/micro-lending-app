package com.github.malipio.micro.lending.app.service;

import java.time.Period;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.malipio.micro.lending.app.domain.Loan;

@Component
public class LoanExtensionService {

	private static final Logger log = LoggerFactory.getLogger(LoanExtensionService.class);
	
	@Value("#{T(java.time.Period).parse('${app.extension.period}')}")
	private Period extensionPeriod;
	
	@Value("${app.extension.interest.factor}")
	private double extensionInterestFactor;
	
	@Autowired
	private LoanRepository loanRepository;
	
	@Transactional
	// PUT - modyfikacja loan
	public void extendLoan(Loan loan) {
		Loan currentLoan = loanRepository.findOne(loan.getId());
		if(currentLoan == null || currentLoan.isExtended())
			return; // TODO idempotent?
		currentLoan.setExtended(true);
		currentLoan.setInterest(currentLoan.getInterest()*this.extensionInterestFactor);
		currentLoan.setTo(currentLoan.getTo().plus(this.extensionPeriod));
	}
	
}

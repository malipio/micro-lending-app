	package com.github.malipio.micro.lending.app.service;

import java.time.Period;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.malipio.micro.lending.app.domain.Loan;

@Component
public class LoanExtensionService {

	private static final Logger log = LoggerFactory.getLogger(LoanExtensionService.class);
	
	@Value("#{T(java.time.Period).parse('${app.extension.period}')}")
	private Period extensionPeriod;
	
	@Value("${app.extension.interest.factor}")
	private double extensionInterestFactor;
	
	public void extendLoan(Loan currentLoan) {
		if( currentLoan.isExtended()) {
			log.info("loan id={} has already been extended",currentLoan.getId());
			return;
		}
		currentLoan.setExtended(true);
		currentLoan.setInterest(currentLoan.getInterest()*this.extensionInterestFactor);
		currentLoan.setTo(currentLoan.getTo().plus(this.extensionPeriod));
		log.info("loan id={} extended successfully", currentLoan.getId());
	}

	public LoanExtensionService(Period extensionPeriod,
			double extensionInterestFactor) {
		super();
		this.extensionPeriod = extensionPeriod;
		this.extensionInterestFactor = extensionInterestFactor;
	}

	public LoanExtensionService() {
	}
	
}

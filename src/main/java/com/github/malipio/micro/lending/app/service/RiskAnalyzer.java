package com.github.malipio.micro.lending.app.service;

import com.github.malipio.micro.lending.app.domain.LoanApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RiskAnalyzer {

	private static final Logger log = LoggerFactory.getLogger(RiskAnalyzer.class);
	
	@Autowired
	private List<RiskRule> rules;
	
	public boolean checkLoanApplicationApproval(LoanApplication loanApplication) {
		return rules.stream().allMatch(r -> {
		
			boolean result = r.validate(loanApplication);
			log.info("risk check for {} is {}", r.getClass().getName(), result);
			return result;
		});
	}

}

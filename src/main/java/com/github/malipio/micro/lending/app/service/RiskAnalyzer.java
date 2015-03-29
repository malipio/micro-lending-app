package com.github.malipio.micro.lending.app.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.malipio.micro.lending.app.domain.LoanApplication;

@Component
public class RiskAnalyzer {

	private static final Logger log = LoggerFactory.getLogger(RiskAnalyzer.class);
	
	@Value("${app.base.interest}")
	private double baseInterest;
	
	@Autowired
	private List<RiskRule> rules;
	
	public boolean checkLoanApplicationApproval(LoanApplication loanApplication) {
		return rules.stream().allMatch(r -> r.validate(loanApplication));
	}

}

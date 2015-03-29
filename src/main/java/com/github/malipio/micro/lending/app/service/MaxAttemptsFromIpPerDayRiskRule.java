package com.github.malipio.micro.lending.app.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.malipio.micro.lending.app.domain.LoanApplication;

@Component
public class MaxAttemptsFromIpPerDayRiskRule implements RiskRule {

	@Value("${app.risk.max.attempts}")
	private int maxAttempts;

	@Autowired
	private LoanApplicationRepository LoanApplicationRepository;
	
	@Override
	public boolean validate(LoanApplication loanApplication) {
		LocalDate forDate = loanApplication.getCreationDate().toLocalDate();
		return LoanApplicationRepository.countBySourceIpAndCreationDate(loanApplication.getSourceIp(), 
				forDate.getYear(), forDate.getMonthValue(), forDate.getDayOfMonth()) <= maxAttempts;
	}
	
	
}

package com.github.malipio.micro.lending.app.service;

import com.github.malipio.micro.lending.app.domain.LoanApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MaxAttemptsFromIpPerDayRiskRule implements RiskRule {

	@Value("${app.risk.max.attempts}")
	private int maxAttempts;

	@Autowired
	private LoanApplicationRepository loanApplicationRepository;
	
	@Override
	public boolean validate(LoanApplication loanApplication) {
		LocalDate forDate = loanApplication.getCreationDate().toLocalDate();
		return loanApplicationRepository.countBySourceIpAndCreationDate(loanApplication.getSourceIp(), 
				forDate.getYear(), forDate.getMonthValue(), forDate.getDayOfMonth()) < maxAttempts;
	}

	public MaxAttemptsFromIpPerDayRiskRule() {
	}
	
	public MaxAttemptsFromIpPerDayRiskRule(
			int maxAttempts,
			com.github.malipio.micro.lending.app.service.LoanApplicationRepository loanApplicationRepository) {
		this.maxAttempts = maxAttempts;
		this.loanApplicationRepository = loanApplicationRepository;
	}
}

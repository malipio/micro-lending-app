package com.github.malipio.micro.lending.app.service;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.malipio.micro.lending.app.domain.LoanApplication;

@Component
public class TimeWindowGapRiskRule implements RiskRule {

	@Value("${app.risk.deny.time.from}")
	private LocalTime denyFrom;
	
	@Value("${app.risk.deny.time.to}")
	private LocalTime denyTo;

	// TODO verify that denyFrom < denyTo
	
	@Override
	public boolean apply(LoanApplication loanApplication) {
		return loanApplication.getCreationDate().toLocalTime().isBefore(denyFrom) &&
				loanApplication.getCreationDate().toLocalTime().isAfter(denyTo);
	}
	
	
}

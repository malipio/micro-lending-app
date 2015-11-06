package com.github.malipio.micro.lending.app.service;

import com.github.malipio.micro.lending.app.domain.LoanApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalTime;

@Component
public class TimeWindowGapWithMaxAmountRiskRule implements RiskRule {

	@Value("#{T(java.time.LocalTime).parse('${app.risk.deny.time.from}')}")
	private LocalTime denyFrom;
	
	@Value("#{T(java.time.LocalTime).parse('${app.risk.deny.time.to}')}")
	private LocalTime denyTo;
	
	@Value("${app.loan.max.amount}")
	private BigDecimal loanMaxAmount;

	@PostConstruct
	public void verifyParameters() {
		
		if (denyTo.isBefore(denyFrom)) {
			throw new IllegalArgumentException("denyFrom <= denyTo");
		}
	}
	
	@Override
	public boolean validate(LoanApplication loanApplication) {
		return loanMaxAmount.compareTo(loanApplication.getLoan().getAmount()) >= 0 && (
				loanApplication.getCreationDate().toLocalTime().isBefore(denyFrom) ||
				loanApplication.getCreationDate().toLocalTime().isAfter(denyTo));
	}

	public TimeWindowGapWithMaxAmountRiskRule() {
	}
	
	public TimeWindowGapWithMaxAmountRiskRule(LocalTime denyFrom, LocalTime denyTo, BigDecimal loanMaxAmount) {
		this.denyFrom = denyFrom;
		this.denyTo = denyTo;
		this.loanMaxAmount = loanMaxAmount;
		verifyParameters();
	}
	
}

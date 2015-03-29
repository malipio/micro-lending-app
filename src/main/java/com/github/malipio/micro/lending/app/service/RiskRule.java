package com.github.malipio.micro.lending.app.service;

import com.github.malipio.micro.lending.app.domain.LoanApplication;

public interface RiskRule {

	/**
	 * @param loanApplication
	 * @return true if application is to be accepted
	 */
	public boolean validate(LoanApplication loanApplication); 
}

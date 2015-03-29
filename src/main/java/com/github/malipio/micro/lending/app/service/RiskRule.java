package com.github.malipio.micro.lending.app.service;

import com.github.malipio.micro.lending.app.domain.LoanApplication;

public interface RiskRule {

	public boolean apply(LoanApplication loanApplication); 
}

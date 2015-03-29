package com.github.malipio.micro.lending.app.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.malipio.micro.lending.app.domain.LoanApplication;

@Component
public class LoanApplicationService {

	private static final Logger log = LoggerFactory.getLogger(LoanApplicationService.class);
	
	@Value("${app.base.interest}")
	private double baseInterest;
	
	@Autowired
	private RiskAnalyzer riskAnalyzer;
	
	@Autowired
	private LoanApplicationRepository loanApplicationRepository;
	
	@Transactional
	// TODO rest context (client ip)
	public LoanApplication issueLoan(LoanApplication loanApplication) {
		
		loanApplication.setSourceIp("TODO:127.0.0.1");
		loanApplication.setCreationDate(LocalDateTime.now());
		
		if (riskAnalyzer.checkLoanApplicationApproval(loanApplication)) {
			loanApplication.setStatus(LoanApplication.Status.APPROVED);
			loanApplication.getLoan().setInterest(baseInterest);
			loanApplication.getLoan().setExtended(false);
			
		} else {
			loanApplication.setStatus(LoanApplication.Status.REJECTED);
			loanApplication.setLoan(null);
		}
		
		return loanApplicationRepository.save(loanApplication);
	}

	public LoanApplicationService(double baseInterest,
			RiskAnalyzer riskAnalyzer,
			LoanApplicationRepository loanApplicationRepository) {
		super();
		this.baseInterest = baseInterest;
		this.riskAnalyzer = riskAnalyzer;
		this.loanApplicationRepository = loanApplicationRepository;
	}

	public LoanApplicationService() {
	}
	
}

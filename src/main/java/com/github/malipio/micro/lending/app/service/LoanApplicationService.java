package com.github.malipio.micro.lending.app.service;

import com.github.malipio.micro.lending.app.domain.LoanApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

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
	public LoanApplication issueLoan(LoanApplication loanApplication) {
		Objects.requireNonNull(loanApplication);
		Objects.requireNonNull(loanApplication.getClient());
		
		loanApplication.setCreationDate(LocalDateTime.now());
		
		if (riskAnalyzer.checkLoanApplicationApproval(loanApplication)) {
			loanApplication.setStatus(LoanApplication.Status.APPROVED);
			loanApplication.getLoan().setInterest(baseInterest);
			loanApplication.getLoan().setExtended(false);
			
		} else {
			loanApplication.setStatus(LoanApplication.Status.REJECTED);
			loanApplication.setLoan(null);
		}
		
		LoanApplication result = loanApplicationRepository.save(loanApplication);
		log.info("loan application has been {} for client pesel={}, sourceIp={}, loanId={}",
				result.getStatus(),
				result.getClient().getPesel(), result.getSourceIp(), 
				result.getLoan() == null ? null : result.getLoan().getId());
		return result;
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

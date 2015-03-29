package com.github.malipio.micro.lending.app;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import com.github.malipio.micro.lending.app.service.LoanApplicationService;

public class LoanApplicationServiceTests {

	
	private LoanApplicationService searchService;
	
	@Before
	public void setupService() {
		this.searchService = new LoanApplicationService();
		
		// mock RiskAnalyzer
	}
	
	// shouldIssueLoan
	// shouldNotIssueLoanDueToRiskAnalysis
	// shouldNotIssueLoanDueToValidation ?
}

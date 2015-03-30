package com.github.malipio.micro.lending.app.service;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.github.malipio.micro.lending.app.domain.ClientBuilder;
import com.github.malipio.micro.lending.app.domain.LoanApplication;
import com.github.malipio.micro.lending.app.domain.LoanBuilder;
import com.github.malipio.micro.lending.app.domain.LoanApplication.Status;
import com.github.malipio.micro.lending.app.domain.LoanApplicationBuilder;

import static org.mockito.Mockito.*;

public class LoanApplicationServiceTests {

	@Test
	public void shouldIssueLoan() {
		// given
		RiskAnalyzer riskAnalyzer = mock(RiskAnalyzer.class);
		LoanApplicationRepository loanApplicationRepo = mock(LoanApplicationRepository.class);
		double baseInterest = 1.2;
		
		LoanApplicationService service = new LoanApplicationService(baseInterest, riskAnalyzer, loanApplicationRepo);
		
		// when
		when(riskAnalyzer.checkLoanApplicationApproval(anyObject())).thenReturn(true);
		when(loanApplicationRepo.save(any(LoanApplication.class))).thenAnswer( 
				a -> a.getArgumentAt(0, LoanApplication.class));
		LoanApplication app = service.issueLoan(new LoanApplicationBuilder()
			.withLoan(new LoanBuilder().build())
			.withClient(new ClientBuilder()
				.withPesel("1122")
				.build())
			.build());
		
		// then
		assertThat(app, notNullValue());
		assertThat(app.getStatus(), is(Status.APPROVED));
		assertThat(app.getLoan(), notNullValue());
		assertThat(app.getLoan().getInterest(), is(baseInterest));
		assertThat(app.getLoan().isExtended(), is(false));
	}
	
	@Test
	public void shouldNotIssueLoanDueToRiskAnalysis() {
		// given
		RiskAnalyzer riskAnalyzer = mock(RiskAnalyzer.class);
		LoanApplicationRepository loanApplicationRepo = mock(LoanApplicationRepository.class);
		double baseInterest = 1.2;
		
		LoanApplicationService service = new LoanApplicationService(baseInterest, riskAnalyzer, loanApplicationRepo);
		
		// when
		when(riskAnalyzer.checkLoanApplicationApproval(anyObject())).thenReturn(false);
		when(loanApplicationRepo.save(any(LoanApplication.class))).thenAnswer( 
				a -> a.getArgumentAt(0, LoanApplication.class));
		LoanApplication app = service.issueLoan(new LoanApplicationBuilder()
			.withClient(new ClientBuilder()
				.withPesel("1122")
				.build())
			.withLoan(new LoanBuilder().build())
			.build());
		
		// then
		assertThat(app, notNullValue());
		assertThat(app.getStatus(), is(Status.REJECTED));
		assertThat(app.getLoan(), nullValue());
	}
	
//	@Test
	public void shouldNotIssueLoanDueToValidation() {
		// TODO
	}
}

package com.github.malipio.micro.lending.app.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.time.Period;

import org.junit.Test;

import com.github.malipio.micro.lending.app.domain.Loan;
import com.github.malipio.micro.lending.app.domain.LoanBuilder;

public class LoanExtensionServiceTests {

	@Test
	public void shouldExtendLoan() {
		// given
		LocalDateTime now = LocalDateTime.now();
		LoanExtensionService service = new LoanExtensionService(Period.ofDays(7), 1.5);
		Loan loan = new LoanBuilder()
			.withFrom(now)
			.withTo(now.plusDays(2))
			.withInterest(1.1)
			.build();
		// when
		service.extendLoan(loan);
		// then
		assertThat(loan.isExtended(), is(true));
		assertThat(loan.getTo(), is(now.plusDays(2).plusDays(7)));
		assertThat(loan.getInterest(), is(1.1*1.5));
	}
	
	@Test
	public void shouldExtendLoanIdempotently() {
		// given
		LocalDateTime now = LocalDateTime.now();
		LoanExtensionService service = new LoanExtensionService(Period.ofDays(7), 1.5);
		Loan loan = new LoanBuilder()
			.withFrom(now)
			.withTo(now.plusDays(2))
			.withInterest(1.1)
			.build();
		// when
		service.extendLoan(loan);
		service.extendLoan(loan);
		service.extendLoan(loan);
		// then
		assertThat(loan.isExtended(), is(true));
		assertThat(loan.getTo(), is(now.plusDays(2).plusDays(7)));
		assertThat(loan.getInterest(), is(1.1*1.5));
	}
	
}

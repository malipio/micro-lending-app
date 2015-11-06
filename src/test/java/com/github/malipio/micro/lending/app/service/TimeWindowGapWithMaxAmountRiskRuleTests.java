package com.github.malipio.micro.lending.app.service;

import com.github.malipio.micro.lending.app.domain.LoanApplicationBuilder;
import com.github.malipio.micro.lending.app.domain.LoanBuilder;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TimeWindowGapWithMaxAmountRiskRuleTests {

	@Test
	public void shouldBeNegative() {
		TimeWindowGapWithMaxAmountRiskRule rule = new TimeWindowGapWithMaxAmountRiskRule(LocalTime.MIN, LocalTime.MAX, BigDecimal.valueOf(100.0));
		
		boolean result = rule.validate(new LoanApplicationBuilder()
			.withCreationDate(LocalDateTime.now())
			.withLoan(new LoanBuilder()
				.withAmount(BigDecimal.valueOf(100.0))
				.build())
			.build());
		
		assertThat(result, is(false));
	}
	
	@Test
	public void shouldBePositiveBeforeDenyFrom() {
		TimeWindowGapWithMaxAmountRiskRule rule = new TimeWindowGapWithMaxAmountRiskRule(LocalTime.of(1,0), LocalTime.of(2,0), BigDecimal.valueOf(100.0));
		
		boolean result = rule.validate(new LoanApplicationBuilder()
			.withCreationDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(0,59)))
			.withLoan(new LoanBuilder()
				.withAmount(BigDecimal.valueOf(100.0))
				.build())
			.build());
		
		assertThat(result, is(true));
	}
	
	@Test
	public void shouldBePositiveAfterDenyTo() {
		TimeWindowGapWithMaxAmountRiskRule rule = new TimeWindowGapWithMaxAmountRiskRule(LocalTime.of(1,0), LocalTime.of(2,0), BigDecimal.valueOf(100.0));
		
		boolean result = rule.validate(new LoanApplicationBuilder()
			.withCreationDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(2,10)))
			.withLoan(new LoanBuilder()
				.withAmount(BigDecimal.valueOf(100.0))
				.build())
			.build());
		
		assertThat(result, is(true));
	}
}

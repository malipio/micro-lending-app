package com.github.malipio.micro.lending.app.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Test;

import com.github.malipio.micro.lending.app.domain.LoanApplicationBuilder;

public class TimeWindowGapRiskRuleTests {

	@Test
	public void shouldBeNegative() {
		TimeWindowGapRiskRule rule = new TimeWindowGapRiskRule(LocalTime.MIN, LocalTime.MAX);
		
		boolean result = rule.validate(new LoanApplicationBuilder()
			.withCreationDate(LocalDateTime.now())
			.build());
		
		assertThat(result, is(false));
	}
	
	@Test
	public void shouldBePositiveBeforeDenyFrom() {
		TimeWindowGapRiskRule rule = new TimeWindowGapRiskRule(LocalTime.of(1,0), LocalTime.of(2,0));
		
		boolean result = rule.validate(new LoanApplicationBuilder()
			.withCreationDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(0,59)))
			.build());
		
		assertThat(result, is(true));
	}
	
	@Test
	public void shouldBePositiveAfterDenyTo() {
		TimeWindowGapRiskRule rule = new TimeWindowGapRiskRule(LocalTime.of(1,0), LocalTime.of(2,0));
		
		boolean result = rule.validate(new LoanApplicationBuilder()
			.withCreationDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(2,10)))
			.build());
		
		assertThat(result, is(true));
	}
}

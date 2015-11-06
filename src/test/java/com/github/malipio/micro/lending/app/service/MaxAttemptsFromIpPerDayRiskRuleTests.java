package com.github.malipio.micro.lending.app.service;

import com.github.malipio.micro.lending.app.domain.LoanApplicationBuilder;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class MaxAttemptsFromIpPerDayRiskRuleTests {

	
	@Test
	public void shouldBePositive() {

		LoanApplicationRepository repo = mock(LoanApplicationRepository.class);
		MaxAttemptsFromIpPerDayRiskRule rule = new MaxAttemptsFromIpPerDayRiskRule(3, repo);
		
		when(repo.countBySourceIpAndCreationDate(eq("123"), eq(1982), eq(11), eq(13))).thenReturn(2L);
		boolean result = rule.validate(new LoanApplicationBuilder()
			.withCreationDate(LocalDateTime.of(LocalDate.of(1982, 11, 13), LocalTime.now()))
			.withSourceIp("123")
			.build());
		
		assertThat(result, is(true));
	}
	
	@Test
	public void shouldBeNegative() {

		LoanApplicationRepository repo = mock(LoanApplicationRepository.class);
		MaxAttemptsFromIpPerDayRiskRule rule = new MaxAttemptsFromIpPerDayRiskRule(3, repo);
		
		when(repo.countBySourceIpAndCreationDate(eq("123"), eq(1982), eq(11), eq(13))).thenReturn(3L);
		boolean result = rule.validate(new LoanApplicationBuilder()
			.withCreationDate(LocalDateTime.of(LocalDate.of(1982, 11, 13), LocalTime.now()))
			.withSourceIp("123")
			.build());
		
		assertThat(result, is(false));
	}
}

package com.github.malipio.micro.lending.app.itests;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.malipio.micro.lending.app.MicroLendingApplication;
import com.github.malipio.micro.lending.app.domain.Loan;
import com.github.malipio.micro.lending.app.domain.LoanBuilder;
import com.github.malipio.micro.lending.app.service.LoanRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MicroLendingApplication.class)
public class LoanRepositoryTests {

	@Autowired
	private LoanRepository repo;
	
	@Test
	public void shouldSaveNewLoan() {
		
		Loan loan = repo.save(new LoanBuilder()
			.withAmount(BigDecimal.valueOf(100.10))
			.withTo(LocalDateTime.now().plusDays(14))
			.withFrom(LocalDateTime.now())
		.build());
		
		assertThat(loan, notNullValue());
		assertThat(loan.getId(), greaterThan(0L));
		assertThat(loan.getAmount(), notNullValue());
		assertThat(loan.getLoanApplication(), nullValue());
	}
	
}

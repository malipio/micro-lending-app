package com.github.malipio.micro.lending.app.itests;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.malipio.micro.lending.app.MicroLendingApplication;
import com.github.malipio.micro.lending.app.domain.Client;
import com.github.malipio.micro.lending.app.domain.ClientBuilder;
import com.github.malipio.micro.lending.app.domain.LoanApplication;
import com.github.malipio.micro.lending.app.domain.LoanBuilder;
import com.github.malipio.micro.lending.app.domain.LoanApplication.Status;
import com.github.malipio.micro.lending.app.domain.LoanApplicationBuilder;
import com.github.malipio.micro.lending.app.service.ClientRepository;
import com.github.malipio.micro.lending.app.service.LoanApplicationRepository;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MicroLendingApplication.class)
@DirtiesContext
public class LoanApplicationRepositoryTests {

	@Autowired
	private LoanApplicationRepository repo;
	
	@Autowired
	private ClientRepository clientRepo;
	
	private Client dummyClient() {
		return clientRepo.save(new ClientBuilder()
			.withPesel("12345678901")
			.withFirstName("John")
			.withLastName("Doe")
			.withRegistrationDate(LocalDateTime.now())
			.build());
	}
	
	@Test
	public void shouldSaveNewRejectedApplication() {
		
		LoanApplication app = repo.save(new LoanApplicationBuilder()
		.withStatus(Status.REJECTED)
		.withCreationDate(LocalDateTime.now())
		.withSourceIp("127.0.0.2")
		.withClient(dummyClient())
		.build());
		
		assertThat(app, notNullValue());
		assertThat(app.getId(), greaterThan(0L));
		assertThat(app.getClient(), notNullValue());
		assertThat(app.getLoan(), nullValue());
	}
	
	@Test
	public void shouldSaveNewApprovedApplicationWithLoan() {
		
		LoanApplication app = repo.save(new LoanApplicationBuilder()
		.withStatus(Status.APPROVED)
		.withCreationDate(LocalDateTime.now())
		.withSourceIp("127.0.0.2")
		.withClient(dummyClient())
		.withLoan(new LoanBuilder()
			.withAmount(BigDecimal.valueOf(100.10))
			.withFrom(LocalDateTime.now())
			.withTo(LocalDateTime.now().plusDays(14))
			.withInterest(1.2)
			.build())
		.build());
		
		assertThat(app, notNullValue());
		assertThat(app.getId(), greaterThan(0L));
		assertThat(app.getClient(), notNullValue());
		assertThat(app.getLoan(), notNullValue());
	}
	
	
	@Test
	public void shouldCountApplications() {
		LocalDateTime now = LocalDateTime.now();
		Stream.generate(() -> new LoanApplicationBuilder()
		.withStatus(Status.REJECTED)
		.withCreationDate(now)
		.withClient(dummyClient())
		.withSourceIp("127.0.0.2")
		.build()).limit(50).forEach(repo::save);
		
		assertThat(repo.count(), is(50L));
		assertThat(repo.countBySourceIpAndCreationDate("127.0.0.1", 
				now.getYear(), now.getMonthValue(), now.getDayOfMonth()), is(0L));
		assertThat(repo.countBySourceIpAndCreationDate("127.0.0.2", 
				now.getYear(), now.getMonthValue(), now.getDayOfMonth()), is(50L));
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void shouldNotSaveLoanDueToValidation() {
		repo.save(new LoanApplicationBuilder()
		.withStatus(Status.APPROVED)
		.withCreationDate(LocalDateTime.now())
		.withSourceIp("127.0.0.2")
		.withClient(dummyClient())
		.withLoan(new LoanBuilder().build())
		.build());
	}
}

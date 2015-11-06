package com.github.malipio.micro.lending.app.itests;

import com.github.malipio.micro.lending.app.MicroLendingApplication;
import com.github.malipio.micro.lending.app.domain.*;
import com.github.malipio.micro.lending.app.domain.LoanApplication.Status;
import com.github.malipio.micro.lending.app.service.ClientRepository;
import com.github.malipio.micro.lending.app.service.LoanApplicationRepository;
import com.github.malipio.micro.lending.app.service.LoanRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MicroLendingApplication.class)
@WebIntegrationTest(randomPort = true)
public class LoanRepositoryTests {

	@Autowired
	private LoanRepository repo;
	
	@Autowired
	private ClientRepository clientRepo;
	
	@Autowired
	private LoanApplicationRepository loanAppRepo;
	
	@Test
	public void shouldSaveNewLoan() {
		
		Loan loan = repo.save(new LoanBuilder()
			.withAmount(BigDecimal.valueOf(100.10))
			.withTo(LocalDateTime.now().plusDays(14))
			.withInterest(1.5)
			.withFrom(LocalDateTime.now())
		.build());
		
		assertThat(loan, notNullValue());
		assertThat(loan.getId(), greaterThan(0L));
		assertThat(loan.getAmount(), notNullValue());
		assertThat(loan.getLoanApplication(), nullValue());
	}
	
	@Test
	public void shouldFindLoansByPesel() {
		
		Client persistedClient = clientRepo.save(new ClientBuilder()
			.withFirstName("John")
			.withLastName("Doe")
			.withPesel("43111800881")
			.withRegistrationDate(LocalDateTime.now())
			.build());
		
		Stream.generate(() -> {
			LocalDateTime fromDateTime = LocalDateTime.now();
			return new LoanApplicationBuilder()
				.withClient(persistedClient)
				.withStatus(Status.APPROVED)
				.withCreationDate(LocalDateTime.now())
				.withSourceIp("IP")
				.withLoan(new LoanBuilder()
					.withAmount(BigDecimal.valueOf(100.10))
					.withFrom(fromDateTime)
					.withTo(fromDateTime.plusDays(14))
					.withInterest(1.5)
					.build())
				.build();
		}).limit(50).forEach(loanAppRepo::save);
		
		List<Loan> loans = repo.findByLoanApplicationClientPeselOrderByToDesc(persistedClient.getPesel());
		
		assertThat(loans, notNullValue());
		assertThat(loans.size(), is(50));
	}
	
}

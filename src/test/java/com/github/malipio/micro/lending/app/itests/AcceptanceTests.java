package com.github.malipio.micro.lending.app.itests;

import com.github.malipio.micro.lending.app.MicroLendingApplication;
import com.github.malipio.micro.lending.app.domain.ClientBuilder;
import com.github.malipio.micro.lending.app.domain.Loan;
import com.github.malipio.micro.lending.app.domain.LoanApplicationBuilder;
import com.github.malipio.micro.lending.app.domain.LoanBuilder;
import com.github.malipio.micro.lending.app.service.LoanApplicationRepository;
import com.github.malipio.micro.lending.app.service.LoanRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MicroLendingApplication.class)
@WebIntegrationTest
public class AcceptanceTests {

	@Value("${server.port}")
	private int port;
	
	@Value("${server.contextPath}")
	private String contextPath;

	@Value("${app.risk.max.attempts}")
	private int maxAttempts;
	
	@Value("#{T(java.time.LocalTime).parse('${app.risk.deny.time.from}')}")
	private LocalTime denyFrom;
	
	@Value("#{T(java.time.LocalTime).parse('${app.risk.deny.time.to}')}")
	private LocalTime denyTo;
	
	@Value("${app.loan.max.amount}")
	private BigDecimal loanMaxAmount;

    @Autowired
    private LoanRepository loanRepo;

    @Autowired
    private LoanApplicationRepository loanAppRepo;

    private static AtomicLong peselGen = new AtomicLong(12345678901L);
	
	private String buildBaseUri() {
		return "http://localhost:" + this.port+"/"+contextPath;
	}
	
	private String buildLoansApplicationUri() {
		return buildBaseUri()+"/loans/applications";
	}
	
	
	private ResponseEntity<?> postNewLoan(String pesel, double amount) {
		return new TestRestTemplate().postForEntity(
				buildLoansApplicationUri(),
				new LoanApplicationBuilder()
						.withClient(
								new ClientBuilder().withPesel(pesel)
										.withFirstName("John")
										.withLastName("Doe").build())
						.withLoan(
								new LoanBuilder()
										.withAmount(BigDecimal.valueOf(amount))
										.withFrom(LocalDateTime.now())
										.withTo(LocalDateTime.now().plusMonths(
												1)).build()).build(),
				Void.class);
	}
	
	private ResponseEntity<?> postNewLoan(String pesel) {
		return postNewLoan(pesel, 100.0);
	}

    private static String nextPesel() {
        return ""+peselGen.incrementAndGet();
    }

    @Before
    public void deleteLoans() {
        loanAppRepo.deleteAll();
        loanRepo.deleteAll();
    }

	@Test
	public void shouldClientApplyForLoanForNewClientPositive() {
		ResponseEntity<?> entity = postNewLoan(nextPesel(), 100.0);
		assertThat(entity.getStatusCode(), is(HttpStatus.CREATED));
		assertThat(entity.getHeaders().getLocation(), notNullValue());
	}
	
	@Test
	public void shouldClientApplyForLoanForNewClientWithMaxAmount() {
		ResponseEntity<?> entity = postNewLoan(nextPesel(), loanMaxAmount.doubleValue());
		LocalTime now = LocalTime.now();
		if(now.isBefore(denyFrom) || now.isAfter(denyTo)) {
			assertThat(entity.getStatusCode(), is(HttpStatus.CREATED));
			assertThat(entity.getHeaders().getLocation(), notNullValue());
		} else {
			assertThat(entity.getStatusCode(), is(HttpStatus.FORBIDDEN));
			assertThat(entity.getHeaders().getLocation(), nullValue());
		}
	}
	
	@Test
	public void shouldClientApplyForLoanForNewClientExceedingMaxAmount() {
		ResponseEntity<?> entity = postNewLoan(nextPesel(), loanMaxAmount.doubleValue()+10.0);
		assertThat(entity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
	}
	
	@Test
	public void shouldClientApplyForLoanForExistingClientNegativeDueToMaxAttempts() {
        String pesel = nextPesel();
        IntStream.range(0, maxAttempts).forEach((int i) -> postNewLoan(pesel));

		ResponseEntity<?> entity = postNewLoan(pesel);
		assertThat(entity.getStatusCode(), is(HttpStatus.FORBIDDEN));
		assertThat(entity.getHeaders().getLocation(), nullValue());
	}
	
	@Test
	public void shouldClientExtendLoan() {
		
		ResponseEntity<?> entity = postNewLoan(nextPesel());
		URI loanUri = entity.getHeaders().getLocation();
		
		new TestRestTemplate().put(
				loanUri+"/extension", null);
		
		new TestRestTemplate().put(
				loanUri+"/extension", null); // idempotent call
	}
	
	@Test
	public void shouldClientRetrieveLoansHistory() {
		TestRestTemplate rest = new TestRestTemplate();
        String pesel = nextPesel();
		postNewLoan(pesel);
		postNewLoan(pesel);
		
		ResponseEntity<Loan[]> response = rest.getForEntity(buildBaseUri()+"/loans?pesel="+pesel, Loan[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(2));
	}
	
	@Test
	public void shouldNotClientRetrieveLoansHistory() {
		
		TestRestTemplate rest = new TestRestTemplate();
		ResponseEntity<Loan[]> response = rest.getForEntity(buildBaseUri()+"/loans?pesel=123", Loan[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
	}
	
	@Test
	public void shouldNotClientApplyForLoanDueToIncorrectPesel() {
		
		ResponseEntity<?> entity = postNewLoan("123");
		assertThat(entity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
	}
}

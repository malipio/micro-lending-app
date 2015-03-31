package com.github.malipio.micro.lending.app.itests;

import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.github.malipio.micro.lending.app.MicroLendingApplication;
import com.github.malipio.micro.lending.app.domain.ClientBuilder;
import com.github.malipio.micro.lending.app.domain.Loan;
import com.github.malipio.micro.lending.app.domain.LoanApplicationBuilder;
import com.github.malipio.micro.lending.app.domain.LoanBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MicroLendingApplication.class)
@WebAppConfiguration
@IntegrationTest
public class AcceptanceTests {

	@Value("${server.port}")
	private int port;
	
	@Value("${server.contextPath}")
	private String contextPath;

	
	private String buildBaseUri() {
		return "http://localhost:" + this.port+"/"+contextPath;
	}
	
	private String buildLoansApplicationUri(String pesel) {
		return buildBaseUri()+"/clients/"+pesel+"/loans/applications";
	}
	
	
	private ResponseEntity<?> postNewLoan(String pesel) {
		return new TestRestTemplate().postForEntity(
				buildLoansApplicationUri(pesel),
				new LoanApplicationBuilder()
						.withClient(
								new ClientBuilder().withPesel(pesel)
										.withFirstName("John")
										.withLastName("Doe").build())
						.withLoan(
								new LoanBuilder()
										.withAmount(BigDecimal.TEN)
										.withFrom(LocalDateTime.now())
										.withTo(LocalDateTime.now().plusMonths(
												1)).build()).build(),
				Void.class);
	}
	
	@Test
	@DirtiesContext
	public void shouldClientApplyForLoanForNewClientPositiveWithReference() {
		ResponseEntity<?> entity = postNewLoan("12345678901");
		assertThat(entity.getStatusCode(), is(HttpStatus.CREATED));
		assertThat(entity.getHeaders().getLocation(), notNullValue());
	}
	
	@Test
	@DirtiesContext
	public void shouldClientApplyForLoanForExistingClientNegativeWithRejection() {
		postNewLoan("12345678901");
		postNewLoan("12345678901");
		postNewLoan("12345678901");
		ResponseEntity<?> entity = postNewLoan("12345678901");
		assertThat(entity.getStatusCode(), is(HttpStatus.FORBIDDEN));
		assertThat(entity.getHeaders().getLocation(), nullValue());
	}
	
	@Test
	@DirtiesContext
	public void shouldClientExtendLoan() {
		
		ResponseEntity<?> entity = postNewLoan("12345678901");
		URI loanUri = entity.getHeaders().getLocation();
		
		new TestRestTemplate().put(
				loanUri+"/extension", null);
		
		new TestRestTemplate().put(
				loanUri+"/extension", null); // idempotent call
	}
	
	@Test
	@DirtiesContext
	public void shouldClientRetrieveLoansHistory() {
		
		TestRestTemplate rest = new TestRestTemplate();
		postNewLoan("12345678901");
		postNewLoan("12345678901");
		
		ResponseEntity<Loan[]> response = rest.getForEntity(buildBaseUri()+"/clients/{pesel}/loans", Loan[].class, "12345678901");
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(2));
	}
	
	@Test
	@DirtiesContext
	public void shouldNotClientRetrieveLoansHistory() {
		
		postNewLoan("12345678901");
		TestRestTemplate rest = new TestRestTemplate();
		ResponseEntity<Loan[]> response = rest.getForEntity(buildBaseUri()+"/clients/{pesel}/loans", Loan[].class, "123");
		assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
	}
	
	@Test
	@DirtiesContext
	public void shouldNotClientApplyForLoanDueToIncorrectPesel() {
		
		ResponseEntity<?> entity = postNewLoan("123");
		assertThat(entity.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR)); // not happy about that
	}
}

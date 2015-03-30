package com.github.malipio.micro.lending.app.itests;

import static org.junit.Assert.assertEquals;

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
import com.github.malipio.micro.lending.app.domain.LoanApplicationBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MicroLendingApplication.class)
@WebAppConfiguration
@IntegrationTest
@DirtiesContext
public class AcceptanceTests {

	@Value("${server.port}")
	private int port;
	
	@Value("${server.contextPath}")
	private String contextPath;

	@Test
	public void shouldClientApplyForLoanForNewClientPositiveWithReference() {
		ResponseEntity<?> entity = new TestRestTemplate().postForEntity(
				"http://localhost:" + this.port+"/"+contextPath+"/clients/1122334455/loans/applications",
				new LoanApplicationBuilder()
					.withClient(new ClientBuilder()
						.withPesel("1122334455")
						.withFirstName("John")
						.withLastName("Doe")
						.build())
					.build(),
				Void.class);
		assertEquals(HttpStatus.CREATED, entity.getStatusCode());
	}
	// shouldClientApplyForLoanForExistingClientNegativeWithRejection
	// shouldClientExtendLoan
	// shouldClientRetrieveLoansHistory
}

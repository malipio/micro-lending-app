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
	public void shouldOpenStartPage() throws Exception {
		ResponseEntity<String> entity = new TestRestTemplate().getForEntity(
				"http://localhost:" + this.port+"/"+contextPath, String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
	}
	
	// shouldClientApplyForLoanForNewClientPositiveWithReference
	// shouldClientApplyForLoanForExistingClientNegativeWithRejection
	// shouldClientExtendLoan
	// shouldClientRetrieveLoansHistory
}

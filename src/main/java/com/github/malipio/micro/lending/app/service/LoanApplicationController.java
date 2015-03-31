package com.github.malipio.micro.lending.app.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.github.malipio.micro.lending.app.domain.Client;
import com.github.malipio.micro.lending.app.domain.LoanApplication;
import com.github.malipio.micro.lending.app.domain.LoanApplication.Status;
import com.github.malipio.micro.lending.app.domain.validator.groups.RequestScope;

@RestController
@RequestMapping("/loans/applications")
public class LoanApplicationController {
	private static final Logger log = LoggerFactory.getLogger(LoanApplicationController.class);
	
	@Autowired
	private LoanApplicationService service;
	
	@Autowired
	private ClientRepository clientRepo;
	
	private static String extractSourceIp(HttpServletRequest httpRequest) {
		return httpRequest.getRemoteAddr();
	}
	
	private Optional<Client> findOrCreateClient(Client client) {
		Client currentClient = clientRepo.findOne(client.getPesel());
		
		if (currentClient == null) {
			log.info("creating new client with pesel={}",client.getPesel());
			client.setRegistrationDate(LocalDateTime.now());
			return Optional.of(clientRepo.save(client));
		} else {
			return Optional.of(currentClient);
		}
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> create(HttpServletRequest httpRequest, @RequestBody @Validated(RequestScope.class) LoanApplication loanApplication) {
		
		Optional<Client> clientOrEmpty = findOrCreateClient(loanApplication.getClient());
		if(!clientOrEmpty.isPresent()) {
			log.info("cannot create client - insufficient data in request");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		loanApplication.setClient(clientOrEmpty.get());
		loanApplication.setSourceIp(extractSourceIp(httpRequest));
		log.info("found sourceIp={}", loanApplication.getSourceIp());
		LoanApplication processedLoan = service.issueLoan(loanApplication);
		if(processedLoan.getStatus() == Status.APPROVED) {
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/loans/{loanId}")
					.buildAndExpand(processedLoan.getLoan().getId()).toUri());
			return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}
}

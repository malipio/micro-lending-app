package com.github.malipio.micro.lending.app.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.github.malipio.micro.lending.app.domain.Client;
import com.github.malipio.micro.lending.app.domain.LoanApplication;
import com.github.malipio.micro.lending.app.domain.LoanApplication.Status;

@RestController
@RequestMapping("/clients/{pesel}/loan-applications")
public class LoanApplicationController {

	@Autowired
	private LoanApplicationService service;
	
	@Autowired
	private ClientRepository clientRepo;
	
	/**
	 * @see <a href="http://stackoverflow.com/questions/10363069/how-can-i-retrieve-ip-address-from-http-header-in-java">Stack overflow answer</a>
	 */
	private static String extractSourceIp(HttpHeaders requestHeaders) {
		
		List<String> remoteAddr = requestHeaders.get("Remote_Addr");
		return remoteAddr.size() == 0 ? requestHeaders.get("HTTP_X_FORWARDED_FOR").get(0) : remoteAddr.get(0);
	}
	
	
	private boolean validateClientInRequest(String pesel, LoanApplication loanApplication) {
		
		if (loanApplication.getClient() != null) {
			return pesel.equals(loanApplication.getClient().getPesel());
		} else
			return true;
	}
	
	private Optional<Client> findOrCreateClient(String pesel, Client client) {
		Client currentClient = clientRepo.findOne(pesel);
		
		if(currentClient == null) {
			if(client != null)
				return Optional.of(clientRepo.save(client));
			else
				return Optional.empty();
		} else
			return Optional.of(currentClient);
	}
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> create(@RequestHeader HttpHeaders requestHeaders, @PathVariable("pesel") String pesel, @RequestBody @Valid LoanApplication loanApplication) {
		
		if(!validateClientInRequest(pesel, loanApplication))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Optional<Client> clientOrEmpty = findOrCreateClient(pesel, loanApplication.getClient());
		if(!clientOrEmpty.isPresent())
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		loanApplication.setClient(clientOrEmpty.get());
		loanApplication.setSourceIp(extractSourceIp(requestHeaders));
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

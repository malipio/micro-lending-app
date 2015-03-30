package com.github.malipio.micro.lending.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.malipio.micro.lending.app.domain.Loan;

@RestController
@RequestMapping("/clients")
public class ClientController {

	@Autowired
	private ClientRepository clientRepo;
	
	@Autowired
	private LoanRepository loanRepo;
	
	@RequestMapping(value="/{pesel}/loans", method=RequestMethod.GET)
	public ResponseEntity<List<Loan>> getHistoryOfLoans(String pesel) {
		
		if(!clientRepo.exists(pesel))
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(loanRepo.findByLoanApplicationClientPeselOrderByToDesc(pesel), HttpStatus.OK);
	}
}

package com.github.malipio.micro.lending.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.malipio.micro.lending.app.domain.Loan;

@RestController
@RequestMapping("/loans/{loanId}")
public class LoanExtensionController {

	@Autowired
	private LoanExtensionService service;
	
	@Autowired
	private LoanRepository repo;
	
	@RequestMapping(value="/extension", method=RequestMethod.PUT)
	@Transactional
	public ResponseEntity<?> extendLoan(@PathVariable("loanId") long loanId) {
		Optional<Loan> loanOrEmpty = Optional.ofNullable(repo.findOne(loanId));
		if(loanOrEmpty.isPresent()) { 
			service.extendLoan(loanOrEmpty.get());
			return new ResponseEntity<>(HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
	}
}

package com.github.malipio.micro.lending.app.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Client {
	
	@Id
	private String pesel;
	
	@NotEmpty
	@Column(nullable=false)
	private String firstName;
	
	@NotEmpty
	@Column(nullable=false)
	private String lastName;
	
	@NotEmpty
	@Column(nullable=false)
	private LocalDateTime registrationDate;
	
	@OneToMany(mappedBy="client")
	private List<LoanApplication> loanApplications;
}

package com.github.malipio.micro.lending.app.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class LoanApplication {
	
	@Id
	@GeneratedValue
	private long id;
	
	public static enum Status {
		REJECTED, APPROVED;
	}
	
	@NotEmpty
	@Column(nullable=false)
	private Status status;
	
	@NotEmpty
	@Column(nullable=false)
	private String sourceIp;
	
	@NotEmpty
	@Column(nullable=false)
	private LocalDateTime creationDate;
	
	@OneToOne(optional = true, fetch = FetchType.EAGER)
	private Loan loan;
	
	@ManyToOne
	private Client client;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public Loan getLoan() {
		return loan;
	}

	public void setLoan(Loan loan) {
		this.loan = loan;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
}

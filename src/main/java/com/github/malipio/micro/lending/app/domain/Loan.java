package com.github.malipio.micro.lending.app.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Loan {
	
	@Id
	@GeneratedValue
	private long id;
	
	@NotEmpty
	@Column(nullable=false)
	private LocalDateTime from;
	
	@NotEmpty
	@Column(nullable=false)
	private LocalDateTime to;
	
	@NotEmpty
	@Column(nullable=false)
	private boolean extended;
	
	@NotEmpty
	@Column(nullable=false)
	private BigDecimal amount;
	
	@NotEmpty
	@Column(nullable=false)
	private double interest;
	
	@OneToOne(mappedBy="loan")
	private LoanApplication loanApplication;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getFrom() {
		return from;
	}

	public void setFrom(LocalDateTime from) {
		this.from = from;
	}

	public LocalDateTime getTo() {
		return to;
	}

	public void setTo(LocalDateTime to) {
		this.to = to;
	}

	public boolean isExtended() {
		return extended;
	}

	public void setExtended(boolean extended) {
		this.extended = extended;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public LoanApplication getLoanApplication() {
		return loanApplication;
	}

	public void setLoanApplication(LoanApplication loanApplication) {
		this.loanApplication = loanApplication;
	}
	
}
package com.github.malipio.micro.lending.app.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.malipio.micro.lending.app.domain.validator.LoanPeriod;

import net.karneim.pojobuilder.GeneratePojoBuilder;

@Entity
@GeneratePojoBuilder
@LoanPeriod
public class Loan {
	
	@Id
	@GeneratedValue
	private long id;
	
	@NotNull
	@Column(nullable=false,name="fromTs")
	private LocalDateTime from;
	
	@NotNull
	@Column(nullable=false,name="toTs")
	private LocalDateTime to;
	
	@Column(nullable=false)
	private boolean extended;
	
	@NotNull
	@Column(nullable=false)
	private BigDecimal amount;
	
	@Column(nullable=false)
	private double interest;
	
	@OneToOne(mappedBy="loan",optional=true)
	@JsonIgnore
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
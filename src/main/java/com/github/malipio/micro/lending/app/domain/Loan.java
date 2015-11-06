package com.github.malipio.micro.lending.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.malipio.micro.lending.app.domain.validator.LoanMaxAmount;
import com.github.malipio.micro.lending.app.domain.validator.LoanPeriod;
import com.github.malipio.micro.lending.app.domain.validator.groups.RequestScope;
import net.karneim.pojobuilder.GeneratePojoBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@GeneratePojoBuilder
@LoanPeriod
public class Loan {
	
	@Id
	@GeneratedValue
	private long id;
	
	@NotNull(groups={Default.class, RequestScope.class})
	@Column(nullable=false,name="fromTs")
	private LocalDateTime from;
	
	@NotNull(groups={Default.class, RequestScope.class})
	@Column(nullable=false,name="toTs")
	private LocalDateTime to;
	
	@Column(nullable=false)
	private boolean extended;
	
	@NotNull
	@LoanMaxAmount(max = 1000.0,groups={Default.class, RequestScope.class})
	@Column(nullable=false)
	private BigDecimal amount;
	
	@Column(nullable=false)
	@NotNull
	@Null(groups=RequestScope.class)
	private Double interest;
	
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
	
	public Double getInterest() {
		return interest;
	}

	public void setInterest(Double interest) {
		this.interest = interest;
	}

	public LoanApplication getLoanApplication() {
		return loanApplication;
	}

	public void setLoanApplication(LoanApplication loanApplication) {
		this.loanApplication = loanApplication;
	}
	
}
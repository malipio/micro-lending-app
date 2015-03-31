package com.github.malipio.micro.lending.app.domain;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.groups.Default;

import net.karneim.pojobuilder.GeneratePojoBuilder;

import org.hibernate.validator.constraints.NotEmpty;

import com.github.malipio.micro.lending.app.domain.validator.groups.RequestScope;

@Entity
@GeneratePojoBuilder
public class LoanApplication {
	
	@Id
	@GeneratedValue
	private long id;
	
	public static enum Status {
		REJECTED, APPROVED;
	}
	
	@NotNull
	@Null(groups=RequestScope.class)
	@Column(nullable=false)
	private Status status;
	
	@NotEmpty
	@Null(groups=RequestScope.class)
	@Column(nullable=false)
	private String sourceIp;
	
	@NotNull
	@Null(groups=RequestScope.class)
	@Column(nullable=false)
	private LocalDateTime creationDate;
	
	@OneToOne(optional = true, fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@NotNull(groups=RequestScope.class)
	@Valid
	private Loan loan;
	
	@ManyToOne(optional=false)
	@NotNull(groups={Default.class, RequestScope.class})
	@Valid
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

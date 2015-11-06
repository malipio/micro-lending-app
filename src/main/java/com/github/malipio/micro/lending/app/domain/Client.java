package com.github.malipio.micro.lending.app.domain;

import com.github.malipio.micro.lending.app.domain.validator.groups.RequestScope;
import net.karneim.pojobuilder.GeneratePojoBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@GeneratePojoBuilder
public class Client {
	
	@Id
	@Size(min=11,max=11,groups={Default.class, RequestScope.class})
	private String pesel;
	
	@NotEmpty
	@Column(nullable=false)
	private String firstName;
	
	@NotEmpty
	@Column(nullable=false)
	private String lastName;
	
	@NotNull
	@Null(groups=RequestScope.class)
	@Column(nullable=false)
	private LocalDateTime registrationDate;
	
	@OneToMany(mappedBy="client")
	@Null(groups=RequestScope.class)
	private List<LoanApplication> loanApplications;

	public String getPesel() {
		return pesel;
	}

	public void setPesel(String pesel) {
		this.pesel = pesel;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDateTime registrationDate) {
		this.registrationDate = registrationDate;
	}

	public List<LoanApplication> getLoanApplications() {
		return loanApplications;
	}

	public void setLoanApplications(List<LoanApplication> loanApplications) {
		this.loanApplications = loanApplications;
	}
	
}

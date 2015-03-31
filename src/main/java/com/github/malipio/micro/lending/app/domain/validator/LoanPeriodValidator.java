package com.github.malipio.micro.lending.app.domain.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.github.malipio.micro.lending.app.domain.Loan;

public class LoanPeriodValidator implements ConstraintValidator<LoanPeriod, Loan>{

	@Override
	public void initialize(LoanPeriod constraintAnnotation) {
	}

	@Override
	public boolean isValid(Loan value, ConstraintValidatorContext context) {
		
		return value.getFrom() != null && value.getTo() != null 
				&& value.getFrom().isBefore(value.getTo());
	}

}

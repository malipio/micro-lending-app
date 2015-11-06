package com.github.malipio.micro.lending.app.domain.validator;

import com.github.malipio.micro.lending.app.domain.Loan;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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

package com.github.malipio.micro.lending.app.domain.validator;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

@Component
public class LoanMaxAmountValidator implements ConstraintValidator<LoanMaxAmount, BigDecimal>{

	private BigDecimal loanMaxAmount;
	
	@Override
	public void initialize(LoanMaxAmount constraintAnnotation) {
		loanMaxAmount = BigDecimal.valueOf(constraintAnnotation.max());
	}

	@Override
	public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
		return value != null && value.compareTo(loanMaxAmount) <= 0;
	}


}

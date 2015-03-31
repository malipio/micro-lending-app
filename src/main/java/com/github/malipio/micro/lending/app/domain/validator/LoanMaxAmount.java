package com.github.malipio.micro.lending.app.domain.validator;

import java.lang.annotation.*;
import java.math.BigDecimal;

import javax.validation.Constraint;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=LoanMaxAmountValidator.class)
public @interface LoanMaxAmount {
	String message() default "Loan amount exceeds maximum";
	Class<?>[] groups() default {};
	Class<?>[] payload() default {};
	double max();
}

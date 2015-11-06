package com.github.malipio.micro.lending.app.domain.validator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=LoanMaxAmountValidator.class)
public @interface LoanMaxAmount {
	String message() default "Loan amount exceeds maximum";
	Class<?>[] groups() default {};
	Class<?>[] payload() default {};
	double max();
}

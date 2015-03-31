package com.github.malipio.micro.lending.app.domain.validator;

import java.lang.annotation.*;

import javax.validation.Constraint;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=LoanPeriodValidator.class)
public @interface LoanPeriod {
	String message() default "Loan period is invalid";
	Class<?>[] groups() default {};
	Class<?>[] payload() default {};
}

package com.github.malipio.micro.lending.app.domain.validator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=LoanPeriodValidator.class)
public @interface LoanPeriod {
	String message() default "Loan period is invalid";
	Class<?>[] groups() default {};
	Class<?>[] payload() default {};
}

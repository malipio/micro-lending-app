package com.github.malipio.micro.lending.app.service;

import com.github.malipio.micro.lending.app.domain.LoanApplication;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface LoanApplicationRepository extends CrudRepository<LoanApplication, Long> {

	@Query("select count(la) from LoanApplication la where "
			+ "la.sourceIp = :sourceIp and YEAR(la.creationDate) = :year and"
			+ " MONTH(la.creationDate) = :month and DAY(la.creationDate) = :day")
	public long countBySourceIpAndCreationDate(@Param("sourceIp") String sourceIp, 
			@Param("year") int year, @Param("month") int month, @Param("day") int day);
}

package com.anuchito.database.person;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.anuchito.database.model.Person;

public interface PersonRepository extends JpaRepository<Person, String> {
    Optional<Person> findByPersonId(String personId);

    @Query(value = "SELECT p.id, p.person_id, p.name, p.age " +
                  "FROM person p " +
                  "WHERE p.person_id = :personId", nativeQuery = true)
    Map<String, Object> findPersonData(@Param("personId") String personId);

    @Query(value = "SELECT l.id, l.loan_id as loanId, l.applicant_name as applicantName, " +
                  "l.loan_amount as loanAmount, l.loan_term as loanTerm, l.status, l.interest_rate as interestRate " +
                  "FROM loan l " +
                  "WHERE l.person_id = :personId", nativeQuery = true)
    List<Map<String, Object>> findLoansByPersonId(@Param("personId") String personId);
}

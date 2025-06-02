package com.anuchito.database.testutil;

import com.anuchito.database.model.Person;
import com.anuchito.database.dto.LoanDto;
import com.anuchito.database.model.Loan;

public class TestDataHelper {

    public static Person createPerson(Long id, String personId, String name, int age) {
        Person person = new Person();
        person.setId(id);
        person.setPersonId(personId);
        person.setName(name);
        person.setAge(age);
        return person;
    }

    public static Loan createTestLoan(String loanId, String personId, double amount) {
        Loan loan = new Loan();
        if (loanId != null) {
            loan.setLoanId(loanId);
        }
        loan.setPersonId(personId);
        loan.setLoanAmount(amount);
        loan.setStatus("ACTIVE");
        return loan;
    }

    public static LoanDto createLoanDto(Long id, String loanId, String applicantName, 
                                       double loanAmount, int loanTerm, String status, 
                                       double interestRate) {
        LoanDto loanDto = new LoanDto();
        loanDto.setId(id);
        loanDto.setLoanId(loanId);
        loanDto.setApplicantName(applicantName);
        loanDto.setLoanAmount(loanAmount);
        loanDto.setLoanTerm(loanTerm);
        loanDto.setStatus(status);
        loanDto.setInterestRate(interestRate);
        return loanDto;
    }
}
package com.anuchito.database.loan;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.anuchito.database.model.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    public Optional<Loan> findByLoanId(String loanId);
}


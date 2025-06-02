package com.anuchito.database.loan;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.anuchito.database.model.Loan;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanService loanService;

    private Loan testLoan;

    @BeforeEach
    void setUp() {
        testLoan = new Loan();
        testLoan.setId(1L);
        testLoan.setLoanId("L001");
        testLoan.setLoanAmount(1000.00);
    }

    @Test
    void getAllLoans_shouldReturnAllLoans() {
        // Arrange
        Loan loan2 = new Loan();
        loan2.setId(2L);
        loan2.setLoanId("L002");
        loan2.setLoanAmount(2000.00);
        
        when(loanRepository.findAll()).thenReturn(Arrays.asList(testLoan, loan2));

        // Act
        var result = loanService.getAllLoans();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getLoanId()).isEqualTo("L001");
        assertThat(result.get(1).getLoanId()).isEqualTo("L002");
    }

    @Test
    void getLoanByLoanId_whenFound_shouldReturnLoan() {
        // Arrange
        when(loanRepository.findByLoanId("L001")).thenReturn(Optional.of(testLoan));

        // Act
        var result = loanService.getLoanByLoanId("L001");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getLoanId()).isEqualTo("L001");
    }

    @Test
    void getLoanByLoanId_whenNotFound_shouldReturnEmpty() {
        // Arrange
        when(loanRepository.findByLoanId("NOT_EXIST")).thenReturn(Optional.empty());

        // Act
        var result = loanService.getLoanByLoanId("NOT_EXIST");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void saveLoan_shouldReturnSavedLoan() {
        // Arrange
        Loan newLoan = new Loan();
        newLoan.setLoanAmount(3000.00);
        
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> {
            Loan l = invocation.getArgument(0);
            l.setId(3L); // Simulate ID generation
            l.setLoanId("L003");
            return l;
        });

        // Act
        Loan result = loanService.saveLoan(newLoan);

        // Assert
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getLoanId()).isEqualTo("L003");
        assertThat(result.getLoanAmount()).isEqualTo(3000.00);
    }

    @Test
    void deleteLoan_shouldCallRepository() {
        // Arrange
        Long loanId = 1L;

        // Act
        loanService.deleteLoan(loanId);

        // Assert
        verify(loanRepository).deleteById(loanId);
    }
}
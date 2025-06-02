package com.anuchito.database.loan;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.anuchito.database.model.Loan;
import static com.anuchito.database.testutil.TestDataHelper.createTestLoan;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(LoanController.class)
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    @Autowired
    private ObjectMapper objectMapper;

    private Loan testLoan;

    @BeforeEach
    void setUp() {
        testLoan = createTestLoan("L001", "P001", 1000.00);
    }

    @Test
    void getAllLoans_shouldReturnAllLoans() throws Exception {
        // Arrange
        Loan loan2 = createTestLoan("L002", "P001", 2000.00);
        when(loanService.getAllLoans()).thenReturn(Arrays.asList(testLoan, loan2));

        // Act & Assert
        mockMvc.perform(get("/api/loans"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].loanId").value("L001"))
               .andExpect(jsonPath("$[1].loanId").value("L002"));
    }

    @Test
    void getLoanByLoanId_whenFound_shouldReturnLoan() throws Exception {
        // Arrange
        when(loanService.getLoanByLoanId("L001")).thenReturn(Optional.of(testLoan));

        // Act & Assert
        mockMvc.perform(get("/api/loans/L001"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.loanId").value("L001"))
               .andExpect(jsonPath("$.personId").value("P001"))
               .andExpect(jsonPath("$.loanAmount").value(1000.00));
    }

    @Test
    void getLoanByLoanId_whenNotFound_shouldReturnNull() throws Exception {
        // Arrange
        when(loanService.getLoanByLoanId("NOT_EXIST")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/loans/NOT_EXIST"))
              .andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$").doesNotExist());
    }
}
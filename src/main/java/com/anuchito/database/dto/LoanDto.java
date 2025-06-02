package com.anuchito.database.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDto {
    private Long id;
    private String loanId;
    private String applicantName;
    private double loanAmount;
    private int loanTerm;
    private String status;
    private double interestRate;
}
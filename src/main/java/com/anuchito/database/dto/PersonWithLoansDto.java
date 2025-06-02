package com.anuchito.database.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonWithLoansDto {
    private Long id;
    private String personId;
    private String name;
    private int age;
    private List<LoanDto> loans;
}
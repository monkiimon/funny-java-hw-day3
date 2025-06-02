package com.anuchito.database.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "person_id", length = 50, unique = true, nullable = false)
    private String personId;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String name = "";

    @Column(name = "age", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int age = 0;
    
    // @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    // @JsonManagedReference
    // private List<Loan> loans = new ArrayList<>();
}

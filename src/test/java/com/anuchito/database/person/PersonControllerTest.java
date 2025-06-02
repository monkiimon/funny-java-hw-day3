package com.anuchito.database.person;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.anuchito.database.model.Person;
import com.anuchito.database.dto.PersonWithLoansDto;
import com.anuchito.database.dto.LoanDto;
import static com.anuchito.database.testutil.TestDataHelper.*;


@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

        @Test
    void getAllPersons_shouldReturnAllPersons() throws Exception {
        // Arrange
        Person person1 = createPerson(1L, "P001", "John", 30);
        Person person2 = createPerson(2L, "P002", "Jane", 25);
        when(personService.getAllPersons()).thenReturn(List.of(person1, person2));

        // Act & Assert
        mockMvc.perform(get("/api/persons"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].personId").value("P001"))
               .andExpect(jsonPath("$[1].personId").value("P002"));
    }

    @Test
    void getPersonByPersonId_shouldReturnPerson() throws Exception {
        // Arrange
        Person person = createPerson(1L, "P001", "John", 30);

        when(personService.getPersonByPersonId("P001")).thenReturn(Optional.of(person));

        // Act & Assert
        mockMvc.perform(get("/api/persons/P001"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.personId").value("P001"))
               .andExpect(jsonPath("$.name").value("John"));
    }

@Test
void getPersonByPersonId_whenNotFound_shouldReturn200WithNull() throws Exception {
    // Arrange
    when(personService.getPersonByPersonId("NOT_EXIST")).thenReturn(Optional.empty());

    // Act & Assert
    mockMvc.perform(get("/api/persons/NOT_EXIST"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").doesNotExist());
}

    @Test
    void createPerson_shouldReturnCreatedPerson() throws Exception {
        // Arrange
        Person savedPerson = createPerson(3L, "P003", "New User", 25);
        when(personService.savePerson(any(Person.class))).thenReturn(savedPerson);

        // Act & Assert
        mockMvc.perform(post("/api/persons")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"personId\":\"P003\",\"name\":\"New User\",\"age\":25}"))
              .andExpect(status().isCreated())
              .andExpect(header().exists("Location"))
              .andExpect(header().string("Location", containsString("/api/persons/P003")))
              .andExpect(jsonPath("$.personId").value("P003"));
    }

@Test
void updatePerson_shouldReturnUpdatedPerson() throws Exception {
    // Arrange
    Person updatedPerson = createPerson(1L, "P001", "Updated Name", 35);
    when(personService.updatePerson(eq("P001"), any(Person.class))).thenReturn(updatedPerson);

    // Act & Assert
    mockMvc.perform(put("/api/persons/P001")
           .contentType(MediaType.APPLICATION_JSON)
           .content("{\"name\":\"Updated Name\",\"age\":35}"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.personId").value("P001"))
           .andExpect(jsonPath("$.name").value("Updated Name"));
}

@Test
void deletePerson_shouldReturnNoContent() throws Exception {
    // Arrange
    doNothing().when(personService).deletePerson("P001");

    // Act
    MvcResult result = mockMvc.perform(delete("/api/persons/P001"))
           .andReturn();
    
    // Assert
    assertThat(result.getResponse().getStatus()).isEqualTo(204);
}

    @Test
    void getPersonWithLoans_shouldReturnPersonWithLoans() throws Exception {
        // Arrange
        PersonWithLoansDto mockPerson = new PersonWithLoansDto();
        mockPerson.setId(1L);
        mockPerson.setPersonId("P001");
        mockPerson.setName("Test User");
        mockPerson.setAge(30);
        
        LoanDto loan1 = new LoanDto();
        loan1.setId(1L);
        loan1.setLoanId("L001");
        loan1.setApplicantName("Test User");
        loan1.setLoanAmount(10000.0);
        loan1.setLoanTerm(12);
        loan1.setStatus("APPROVED");
        loan1.setInterestRate(7.5);
        
        mockPerson.setLoans(Arrays.asList(loan1));

        when(personService.getPersonWithLoans("P001")).thenReturn(mockPerson);

        // Act & Assert
        mockMvc.perform(get("/api/persons/P001/loans"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.personId").value("P001"))
               .andExpect(jsonPath("$.name").value("Test User"))
               .andExpect(jsonPath("$.loans[0].loanId").value("L001"))
               .andExpect(jsonPath("$.loans[0].status").value("APPROVED"));
    }

    @Test
    void getPersonWithLoans_whenPersonNotFound_shouldReturn404() throws Exception {
        // Arrange
        when(personService.getPersonWithLoans("NOT_EXIST")).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/persons/NOT_EXIST/loans"))
              .andExpect(status().isNotFound())
              .andExpect(jsonPath("$.message").value("Person not found with id: NOT_EXIST"));
    }
}
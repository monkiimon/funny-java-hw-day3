package com.anuchito.database.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import com.anuchito.database.model.Person;
import com.anuchito.database.person.PersonRepository;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person();
        person.setId(1L);
        person.setPersonId("P001");
        person.setName("John Doe");
        person.setAge(30);
    }

    @Test
    void getAllPersons_shouldReturnAllPersons() {
        // Arrange
        Person person2 = new Person();
        person2.setId(2L);
        person2.setPersonId("P002");
        person2.setName("Jane Doe");
        person2.setAge(25);

        when(personRepository.findAll()).thenReturn(Arrays.asList(person, person2));

        // Act
        List<Person> result = personService.getAllPersons();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPersonId()).isEqualTo("P001");
        assertThat(result.get(1).getPersonId()).isEqualTo("P002");
    }

    @Test
    void getPersonByPersonId_whenFound_shouldReturnPerson() {
        // Arrange
        when(personRepository.findByPersonId("P001")).thenReturn(Optional.of(person));

        // Act
        Optional<Person> result = personService.getPersonByPersonId("P001");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getPersonId()).isEqualTo("P001");
    }

    @Test
    void getPersonByPersonId_whenNotFound_shouldReturnEmpty() {
        // Arrange
        when(personRepository.findByPersonId("NOT_EXIST")).thenReturn(Optional.empty());

        // Act
        Optional<Person> result = personService.getPersonByPersonId("NOT_EXIST");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void savePerson_shouldReturnSavedPerson() {
        // Arrange
        Person newPerson = new Person();
        newPerson.setPersonId("P003");
        newPerson.setName("New User");
        newPerson.setAge(25);

        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> {
            Person p = invocation.getArgument(0);
            p.setId(3L); // Simulate ID generation
            return p;
        });

        // Act
        Person result = personService.savePerson(newPerson);

        // Assert
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getPersonId()).isEqualTo("P003");
    }

    @Test
    void updatePerson_whenExists_shouldUpdateAndReturnPerson() {
        // Arrange
        Person existingPerson = new Person();
        existingPerson.setId(1L);
        existingPerson.setPersonId("P001");
        
        Person updateData = new Person();
        updateData.setName("Updated Name");
        updateData.setAge(35);

        when(personRepository.findByPersonId("P001")).thenReturn(Optional.of(existingPerson));
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Person result = personService.updatePerson("P001", updateData);

        // Assert
        assertThat(result.getName()).isEqualTo("Updated Name");
        assertThat(result.getAge()).isEqualTo(35);
        assertThat(result.getPersonId()).isEqualTo("P001");
    }

    @Test
    void updatePerson_whenNotExists_shouldReturnNull() {
        // Arrange
        when(personRepository.findByPersonId("NOT_EXIST")).thenReturn(Optional.empty());

        // Act
        Person result = personService.updatePerson("NOT_EXIST", new Person());

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void deletePerson_shouldCallRepository() {
        // Arrange - no need to mock anything for void methods with doNothing as default

        // Act
        personService.deletePerson("P001");

        // Assert - verify the method was called
        verify(personRepository).deleteById("P001");
    }
}
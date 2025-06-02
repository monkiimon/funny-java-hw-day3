package com.anuchito.database.person;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anuchito.database.dto.PersonWithLoansDto;
import com.anuchito.database.model.Person;
import java.net.URI;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("/{personId}")
    public Optional<Person> getPersonByPersonId(@PathVariable String personId) {
        return personService.getPersonByPersonId(personId);
    }

    // @GetMapping("/{personId}/loans")
    // public Optional<Person> getPersonByPersonIdWithLoans(@PathVariable String personId) {
    //     return personService.getPersonByPersonIdWithLoans(personId);
    // }

    @GetMapping("/{personId}/loans")
    public ResponseEntity<?> getPersonWithLoans(@PathVariable String personId) {
        PersonWithLoansDto result = personService.getPersonWithLoans(personId);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Person not found with id: " + personId));
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{personId}")
    public ResponseEntity<Void> deletePerson(@PathVariable String personId) {
        personService.deletePerson(personId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        Person savedPerson = personService.savePerson(person);
        return ResponseEntity
                .created(URI.create("/api/persons/" + savedPerson.getPersonId()))
                .body(savedPerson);
    }

    @PutMapping("/{personId}")
    public ResponseEntity<Person> updatePerson(
            @PathVariable String personId,
            @RequestBody Person person) {
        Person updatedPerson = personService.updatePerson(personId, person);
    if (updatedPerson == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(updatedPerson);
}
}

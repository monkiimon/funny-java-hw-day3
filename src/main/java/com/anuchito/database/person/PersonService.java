package com.anuchito.database.person;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.anuchito.database.model.Person;

import com.anuchito.database.dto.PersonWithLoansDto;
import com.anuchito.database.dto.LoanDto;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Optional<Person> getPersonByPersonId(String personId) {
        return personRepository.findByPersonId(personId);
    }

    // public Optional<Person> getPersonByPersonIdWithLoans(String personId) {
    //     return personRepository.findByPersonIdWithLoans(personId);
    // }

    public Optional<Person> getLoanByLoanId(String loanId) {
        return this.personRepository.findByPersonId(loanId);
    }

    public Person savePerson(Person person) {
        return this.personRepository.save(person);
    }

    public Person updatePerson(String personId, Person person) {
        Optional<Person> existingPerson = this.personRepository.findByPersonId(personId);
        if (existingPerson.isPresent()) {
            Person existing = existingPerson.get();
            existing.setName(person.getName());
            existing.setAge(person.getAge());
            return this.personRepository.save(existing);
        }
        return null;
    }

    public void deletePerson(String id) {
        this.personRepository.deleteById(id);
    }

    public PersonWithLoansDto getPersonWithLoans(String personId) {
        // Query 1: get person
        Map<String, Object> personData = personRepository.findPersonData(personId);
        if (personData == null || personData.isEmpty()) {
            return null;  // ตรวจสอบทั้ง null และ empty
        }

        // Query 2: get loans
        List<Map<String, Object>> loansData = personRepository.findLoansByPersonId(personId);
        
        // Map person data
        PersonWithLoansDto result = new PersonWithLoansDto();
        result.setId(personData.get("id") != null ? ((Number)personData.get("id")).longValue() : null);
        result.setPersonId((String) personData.get("person_id"));
        result.setName((String) personData.get("name"));
        result.setAge(personData.get("age") != null ? ((Number)personData.get("age")).intValue() : 0);
        
        // Map loans
        List<LoanDto> loans = loansData.stream()
            .filter(loanData -> loanData != null && !loanData.isEmpty())  // ตรวจสอบ null และ empty
            .map(loanData -> {
                LoanDto loanDto = new LoanDto();
                loanDto.setId(loanData.get("id") != null ? ((Number)loanData.get("id")).longValue() : null);
                loanDto.setLoanId((String) loanData.get("loanId"));
                loanDto.setApplicantName((String) loanData.get("applicantName"));
                loanDto.setLoanAmount(loanData.get("loanAmount") != null ? 
                    ((Number) loanData.get("loanAmount")).doubleValue() : 0.0);
                loanDto.setLoanTerm(loanData.get("loanTerm") != null ? 
                    ((Number) loanData.get("loanTerm")).intValue() : 0);
                loanDto.setStatus((String) loanData.get("status"));
                loanDto.setInterestRate(loanData.get("interestRate") != null ? 
                    ((Number) loanData.get("interestRate")).doubleValue() : 0.0);
                return loanDto;
            })
            .collect(Collectors.toList());
        
        result.setLoans(loans);
        return result;
    }
}

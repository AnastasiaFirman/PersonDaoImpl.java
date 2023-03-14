package org.anastasia.peopleinfoapplication.service;

import org.anastasia.peopleinfoapplication.exception.UserNotFoundException;
import org.anastasia.peopleinfoapplication.model.Person;
import org.anastasia.peopleinfoapplication.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person save(Person person) {
        int age = calcAge(person);
        person.setAge(age);
        return personRepository.save(person);
    }

    @Override
    public Person findById(Long id) {
        return personRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<Person> findAll() {
        List<Person> all = personRepository.findAll();
        return all;
    }

    @Override
    public void deleteById(Long id) {
        try {
            personRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException();
        }

    }

    @Override
    public Person update(Long id, Person person) {
        int age = calcAge(person);
        person.setAge(age);
        person.setId(id);
        return personRepository.save(person);
    }

    private int calcAge(Person person) {
        Period difference = Period.between(person.getDateOfBirth(), LocalDate.now());
        int calculatedAge = difference.getYears();
        return calculatedAge;
    }
}

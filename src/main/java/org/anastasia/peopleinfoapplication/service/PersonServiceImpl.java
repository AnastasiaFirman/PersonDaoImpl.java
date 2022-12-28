package org.anastasia.peopleinfoapplication.service;

import org.anastasia.peopleinfoapplication.dao.PersonDao;
import org.anastasia.peopleinfoapplication.exception.UserNotFoundException;
import org.anastasia.peopleinfoapplication.model.Person;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class PersonServiceImpl implements PersonService {
    private PersonDao personDao;

    public PersonServiceImpl(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Override
    public Person save(Person person) {
        int age = calcAge(person);
        person.setAge(age);
        return personDao.save(person);
    }

    @Override
    public Person findById(Long id) {
        return personDao.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<Person> findAll() {
        return personDao.findAll();
    }

    @Override
    public void deleteAll() {
        personDao.deleteAll();
    }

    @Override
    public void deleteById(Long id) {
        personDao.deleteById(id);
    }

    @Override
    public Person update(Long id, Person person) {
        int age = calcAge(person);
        person.setAge(age);
        return personDao.update(id, person);
    }

    private int calcAge(Person person) {
        Period difference = Period.between(person.getDateOfBirth(), LocalDate.now());
        int calculatedAge = difference.getYears();
        return calculatedAge;
    }
}

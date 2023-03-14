package org.anastasia.peopleinfoapplication.dao;

import org.anastasia.peopleinfoapplication.model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDate;
import java.util.ArrayList;

public class PersonDaoTest extends BaseIntegrationTest{
    private final PersonDao personDao;

    @Autowired
    public PersonDaoTest(@Qualifier("jpaImpl") PersonDao personDao) {
        this.personDao = personDao;
    }

    @AfterEach
    void deleteAll() {
        personDao.deleteAll();
    }

    @Test
    void saveAndFindTest() {
        Person personForSave = new Person("Katya", "Ivanova", 24, LocalDate.now());
        personForSave.setBooks(new ArrayList<>());
        personDao.save(personForSave);
        Person foundPerson = personDao.findById(personForSave.getId()).get();
        Assertions.assertEquals(personForSave, foundPerson);
    }

    @Test
    void findAllTest() {
        personDao.save(new Person("Nadya", "Petrova", 34, LocalDate.now()));
        personDao.save(new Person("Masha", "Lavrova", 20, LocalDate.now()));
        Assertions.assertEquals(2, personDao.findAll().size());
    }

    @Test
    void deleteById() {
        Person person = new Person(1L, "Nadya", "Petrova", 34, LocalDate.now());
        personDao.deleteById(person.getId());
        Assertions.assertEquals(0, personDao.findAll().size());
    }
}
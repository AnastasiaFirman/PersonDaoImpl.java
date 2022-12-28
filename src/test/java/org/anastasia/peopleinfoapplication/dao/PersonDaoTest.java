package org.anastasia.peopleinfoapplication.dao;

import org.anastasia.peopleinfoapplication.dbconnector.PostgresConnector;
import org.anastasia.peopleinfoapplication.model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDate;

public class PersonDaoTest {
    private final PersonDao personDao = new ClassPathXmlApplicationContext("file:src/main/resources/applicationContext.xml")
            .getBean("personDaoImpl", PersonDaoImpl.class);

    @AfterEach
    void deleteAll() {
        personDao.deleteAll();
    }

    @Test
    void saveAndFindTest() {
        Person personForSave = new Person("Katya", "Ivanova", 24, LocalDate.now());
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
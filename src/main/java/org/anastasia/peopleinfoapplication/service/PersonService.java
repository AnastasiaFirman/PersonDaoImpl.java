package org.anastasia.peopleinfoapplication.service;

import org.anastasia.peopleinfoapplication.model.Person;

import java.sql.SQLException;
import java.util.List;

public interface PersonService {
    Person save(Person person) throws SQLException;

    Person findById(Long id) throws SQLException;

    List<Person> findAll() throws SQLException;
    void deleteAll() throws SQLException;

    void deleteById(Long id) throws SQLException;

    Person update(Long id, Person person) throws SQLException;
}

package org.anastasia.peopleinfoapplication.dao;

import org.anastasia.peopleinfoapplication.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonDao {
    Person save(Person person);

    Optional<Person> findById(Long id);

    List<Person> findAll();

    void deleteAll();

    void deleteById(Long id);

    Person update(Long id, Person person);
}

package org.anastasia.peopleinfoapplication.service;

import org.anastasia.peopleinfoapplication.model.Person;

import java.util.List;

public interface PersonService {

    Person save(Person person);

    Person findById(Long id);

    List<Person> findAll();

    void deleteById(Long id);

    Person update(Long id, Person person);
}

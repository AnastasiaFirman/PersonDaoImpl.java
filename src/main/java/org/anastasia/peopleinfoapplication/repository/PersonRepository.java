package org.anastasia.peopleinfoapplication.repository;

import org.anastasia.peopleinfoapplication.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}

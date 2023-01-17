package org.anastasia.peopleinfoapplication.controller;

import org.anastasia.peopleinfoapplication.model.Person;
import org.anastasia.peopleinfoapplication.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/api/v1/person")
    public List<Person> findAll() {
        return personService.findAll();
    }

    @GetMapping("/api/v1/person/{id}")
    public Person findById(@PathVariable("id") Long id) {
        return personService.findById(id);
    }

    @PostMapping("/api/v1/person")
    public Person save(@RequestBody Person person) {
        return personService.save(person);
    }
}

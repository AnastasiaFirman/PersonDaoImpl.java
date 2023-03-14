package org.anastasia.peopleinfoapplication.dao;

import org.anastasia.peopleinfoapplication.model.Person;
import org.hibernate.annotations.Parameter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository("jpaImpl")
public class PersonDaoJpaImpl implements PersonDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Person save(Person person) {
        entityManager.persist(person);
        return person;
    }

    @Override
    public Optional<Person> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Person.class, id));
    }

    @Override
    public List<Person> findAll() {
        TypedQuery<Person> query = entityManager.createQuery("select person from Person person", Person.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void deleteAll() {
        Query deleteQuery = entityManager.createQuery("delete from Person");
        deleteQuery.executeUpdate();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Query query = entityManager.createQuery("delete from Person person where person.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    @Transactional
    public Person update(Long id, Person person) {
        person.setId(id);
        return entityManager.merge(person);
    }
}

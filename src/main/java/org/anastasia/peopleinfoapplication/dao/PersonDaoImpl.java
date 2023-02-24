package org.anastasia.peopleinfoapplication.dao;

import liquibase.pro.packaged.B;
import liquibase.pro.packaged.P;
import org.anastasia.peopleinfoapplication.exception.SQLProcessingException;
import org.anastasia.peopleinfoapplication.model.Book;
import org.anastasia.peopleinfoapplication.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@Repository
public class PersonDaoImpl implements PersonDao {
    private static final String FIND_ALL = "select * from person;";
    private static final String DELETE_ALL = "delete from person;";
    private static final String FIND_BY_ID = "select p.first_name, p.last_name, p.age, p.date_of_birth, b.id book_id, b.title, b.author, b.person_id" +
            " from person p left join books b on p.id = b.person_id where p.id = ?;";
    private static final String SAVE_PERSON =
            "insert into person (first_name, last_name, age, date_of_birth) values(?,?,?,?);";
    private static final String DELETE_BY_ID = "delete from person where id = ?;";

    private static final String UPDATE_BY_ID = "update person set first_name = ?, last_name = ?," +
            "age = ?, date_of_birth = ? where id = ?;";

    @Autowired
    private final DataSource dataSource;

    @Autowired
    public PersonDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Person save(Person person) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_PERSON, Statement.RETURN_GENERATED_KEYS);) {

            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setInt(3, person.getAge());
            preparedStatement.setDate(4, java.sql.Date.valueOf(person.getDateOfBirth()));
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                person.setId(generatedKeys.getLong("id"));
            }
        } catch (SQLException e) {
            throw new SQLProcessingException(e.getMessage());
        }
        return person;
    }

    @Override
    public Optional<Person> findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return buildPerson(resultSet);

        } catch (SQLException e) {
            throw new SQLProcessingException(e.getMessage());
        }
    }

    @Override
    public List<Person> findAll() {
        List<Person> result = new LinkedList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //result.add(buildPerson(resultSet));
            }
        } catch (SQLException e) {
            throw new SQLProcessingException(e.getMessage());
        }
        return result;
    }

    @Override
    public void deleteAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ALL)) {

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLProcessingException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLProcessingException(e.getMessage());
        }
    }

    @Override
    public Person update(Long id, Person person) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BY_ID)) {
            findById(id);

            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setInt(3, person.getAge());
            preparedStatement.setDate(4, java.sql.Date.valueOf(person.getDateOfBirth()));
            preparedStatement.setLong(5, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new SQLProcessingException(e.getMessage());
        }
        return person;
    }

    private Optional<Person> buildPerson(ResultSet resultSet) {
        Person person = new Person();
        List<Book> books = new LinkedList<>();
        person.setBooks(books);
        try {
            if (resultSet.next()) {
                person.setId(resultSet.getLong("person_id"));
                person.setFirstName(resultSet.getString("first_name"));
                person.setLastName(resultSet.getString("last_name"));
                person.setAge(resultSet.getInt("age"));
                person.setDateOfBirth((resultSet.getDate("date_of_birth")).toLocalDate());
                Book book = new Book();
                book.setAuthor(resultSet.getString("author"));
                book.setBookTitle(resultSet.getString("title"));
                book.setId(resultSet.getLong("book_id"));
                books.add(book);
            } else {
                return Optional.empty();
            }
            while (resultSet.next()) {
                Book book = new Book();
                book.setAuthor(resultSet.getString("author"));
                book.setBookTitle(resultSet.getString("title"));
                book.setId(resultSet.getLong("book_id"));
                books.add(book);
            }
        } catch (SQLException e) {
            throw new SQLProcessingException(e.getMessage());
        }
        return Optional.of(person);

    }
        /*Person person = new Person();
        Long currentPersonId = person.getId();
        try {
            while (resultSet.next() && currentPersonId.equals(person.getId())) {
                createPerson(resultSet);
                //createBook(resultSet);
                person.setBooks(createBook(resultSet));
                if (!Objects.equals(currentPersonId, person.getId())) {
                    createPerson(resultSet);
                    person.setBooks(createBook(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new SQLProcessingException(e.getMessage());
        }
        return Optional.of(person);
    }*/


   /* private Optional<Person> createPerson(ResultSet resultSet) {
        Person person = new Person();
        //List<Book> books = new LinkedList<>();
        try {
            if (resultSet.next()) {
                person.setId(resultSet.getLong("person_id"));
                person.setFirstName(resultSet.getString("first_name"));
                person.setLastName(resultSet.getString("last_name"));
                person.setAge(resultSet.getInt("age"));
                person.setDateOfBirth((resultSet.getDate("date_of_birth")).toLocalDate());
                *//*createBook(resultSet);
                person.setBooks(books);*//*
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new SQLProcessingException(e.getMessage());
        }
        return Optional.of(person);
    }

    private List<Book> createBook(ResultSet resultSet) {
        Book book = new Book();
        List<Book> books = new LinkedList<>();
        try {
            while (resultSet.next()) {
                book.setAuthor(resultSet.getString("author"));
                book.setBookTitle(resultSet.getString("title"));
                book.setId(resultSet.getLong("book_id"));
                books.add(book);
            }
        } catch (SQLException e) {
            throw new SQLProcessingException(e.getMessage());
        }
        return books;
    }*/
}
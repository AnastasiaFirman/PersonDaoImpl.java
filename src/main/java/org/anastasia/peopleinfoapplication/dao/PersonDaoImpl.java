package org.anastasia.peopleinfoapplication.dao;

import org.anastasia.peopleinfoapplication.dbconnector.Connector;
import org.anastasia.peopleinfoapplication.exception.SQLProcessingException;
import org.anastasia.peopleinfoapplication.model.Person;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PersonDaoImpl implements PersonDao {
    private static final String FIND_ALL = "select * from person;";
    private static final String DELETE_ALL = "delete from person;";
    private static final String FIND_BY_ID = "select * from person where id = ?;";
    private static final String SAVE_PERSON =
            "insert into person (first_name, last_name, age, date_of_birth) values(?,?,?,?);";
    private static final String DELETE_BY_ID = "delete from person where id = ?;";

    private static final String UPDATE_BY_ID = "update person set first_name = ?, last_name = ?," +
            "age = ?, date_of_birth = ? where id = ?;";
    private Connector connector;

    public PersonDaoImpl(Connector connector) {
        this.connector = connector;
    }

    @Override
    public Person save(Person person) {
        try (Connection connection = connector.getConnection();
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
        Person person;
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                person = buildPerson(resultSet);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new SQLProcessingException(e.getMessage());
        }
        return Optional.of(person);
    }

    @Override
    public List<Person> findAll() {
        List<Person> result = new LinkedList<>();
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(buildPerson(resultSet));
            }
        } catch (SQLException e) {
            throw new SQLProcessingException(e.getMessage());
        }
        return result;
    }

    @Override
    public void deleteAll() {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ALL)) {

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLProcessingException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLProcessingException(e.getMessage());
        }
    }

    @Override
    public Person update(Long id, Person person) {
        try (Connection connection = connector.getConnection();
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

    private Person buildPerson(ResultSet resultSet) {
        Person person = new Person();
        try {
            person.setId(resultSet.getLong("id"));
            person.setFirstName(resultSet.getString("first_name"));
            person.setLastName(resultSet.getString("last_name"));
            person.setAge(resultSet.getInt("age"));
            person.setDateOfBirth((resultSet.getDate("date_of_birth")).toLocalDate());
        } catch (SQLException e) {
            throw new SQLProcessingException(e.getMessage());
        }
        return person;
    }
}

package org.anastasia.peopleinfoapplication.dao;

import liquibase.pro.packaged.B;
import org.anastasia.peopleinfoapplication.exception.SQLProcessingException;
import org.anastasia.peopleinfoapplication.model.Book;
import org.anastasia.peopleinfoapplication.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class PersonDaoImpl implements PersonDao {
    private static final String FIND_ALL = "select p.id person_id, p.first_name, p.last_name, p.age, p.date_of_birth, b.id book_id, b.title, b.author" +
            " from person p left join book b on p.id = b.person_id;";
    private static final String DELETE_ALL = "delete from person;";
    private static final String FIND_BY_ID = "select p.id person_id, p.first_name, p.last_name, p.age, p.date_of_birth, b.id book_id, b.title, b.author" +
            " from person p left join book b on p.id = b.person_id where p.id = ?;";
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
    public Optional<Person> findById(Long personId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, personId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Person person = null;
            List<Book> books = new LinkedList<>();
            boolean personCreated = false;
            while (resultSet.next()) {
                if (!personCreated) {
                    person = createPerson(personId, resultSet);
                    personCreated = true;
                }
                    long bookId = resultSet.getLong("book_id");
                    if (bookId != 0) {
                        Book book = createBook(resultSet, bookId);
                        books.add(book);
                }
                person.setBooks(books);
            }
            return Optional.ofNullable(person);
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
            long currentPersonId = 0L;
            Person person = null;
            while (resultSet.next()) {
                long personId = resultSet.getLong("person_id");
                if (personId != currentPersonId) {
                    currentPersonId = personId;
                    person = createPerson(personId, resultSet);
                    result.add(person);
                }
                long bookId = resultSet.getLong("book_id");
                if (bookId != 0) {
                    person.getBooks().add(createBook(resultSet, bookId));
                }
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

    private Person createPerson(long personId, ResultSet resultSet) throws SQLException {
        Person person = new Person();
        person.setId(personId);
        person.setFirstName(resultSet.getString("first_name"));
        person.setLastName(resultSet.getString("last_name"));
        person.setAge(resultSet.getInt("age"));
        person.setDateOfBirth((resultSet.getDate("date_of_birth")).toLocalDate());
        return person;
    }

    private Book createBook(ResultSet resultSet, long bookId) throws SQLException {
        Book book = new Book();
        book.setId(bookId);
        book.setTitle(resultSet.getString("title"));
        book.setAuthor(resultSet.getString("author"));
        return book;
    }
}


   /* @Override
    public Optional<Person> findById(Long personId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, personId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Person person = null;
            while (resultSet.next()) {
                person = createPerson(personId, resultSet);
                long bookId = resultSet.getLong("book_id");
                if (bookId != 0) {
                    person.getBooks().add(createBook(resultSet, bookId));
                }
            }
            return Optional.ofNullable(person);
        } catch (SQLException e) {
            throw new SQLProcessingException(e.getMessage());
        }
    }*/
   /*@Override
   public Optional<Person> findById(Long personId) {
       try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
           preparedStatement.setLong(1, personId);
           ResultSet resultSet = preparedStatement.executeQuery();
           Person person = null;
           List<Book> books = new LinkedList<>();
           while (resultSet.next()) {
               person = createPerson(personId, resultSet);
               long bookId = resultSet.getLong("book_id");
               Book book = createBook(resultSet, bookId);
               books.add(book);
               if (bookId != 0) {
                   person.setBooks(books);
               }
           }
           return Optional.ofNullable(person);
       } catch (SQLException e) {
           throw new SQLProcessingException(e.getMessage());
       }
   }*/
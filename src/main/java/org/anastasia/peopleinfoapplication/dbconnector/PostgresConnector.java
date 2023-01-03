package org.anastasia.peopleinfoapplication.dbconnector;

import org.anastasia.peopleinfoapplication.exception.DBConnectionException;
import org.anastasia.peopleinfoapplication.exception.ReadPropertyException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PostgresConnector implements Connector {
    @Override
    public Connection getConnection() {
        Properties props = getProperties();
        String url = props.getProperty("url");
        String user = props.getProperty("user");
        String password = props.getProperty("password");

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new DBConnectionException(e.getMessage());
        }
    }

    private Properties getProperties() {
        try (FileInputStream fis = new FileInputStream("src/main/resources/prop.properties")) {
            Properties prop = new Properties();
            prop.load(fis);
            return prop;
        } catch (IOException e) {
            throw new ReadPropertyException(e.getMessage());
        }
    }
}


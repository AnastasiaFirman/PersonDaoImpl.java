package org.anastasia.peopleinfoapplication.dbconnector;

import java.sql.Connection;

public interface Connector {
    Connection getConnection ();
}

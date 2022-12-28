package org.anastasia.peopleinfoapplication;

import org.anastasia.peopleinfoapplication.dao.PersonDao;
import org.anastasia.peopleinfoapplication.dao.PersonDaoImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PeopleInfoApplication {
    public static void main(String[] args) {
        PersonDao personDao = new ClassPathXmlApplicationContext("file:src/main/resources/applicationContext.xml")
                .getBean("personDaoImpl", PersonDaoImpl.class);
    }
}
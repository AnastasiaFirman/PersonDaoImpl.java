package org.anastasia.peopleinfoapplication.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PeopleInfoExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(PeopleInfoExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class})
    public String handleUserNotFoundException() {
        logger.error("Пользователь с таким id не найден");
        return "Пользователь с таким id не найден";
    }
}

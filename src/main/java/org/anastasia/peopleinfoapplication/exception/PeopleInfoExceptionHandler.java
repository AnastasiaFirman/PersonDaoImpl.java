package org.anastasia.peopleinfoapplication.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PeopleInfoExceptionHandler {
    @ExceptionHandler({UserNotFoundException.class})
    public String handlerUserNonFoundException() {
        return "Пользователь с таким id не найден";
    }
}

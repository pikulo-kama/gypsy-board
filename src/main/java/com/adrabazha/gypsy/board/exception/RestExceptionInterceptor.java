package com.adrabazha.gypsy.board.exception;

import com.adrabazha.gypsy.board.dto.UserMessage;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionInterceptor {

    @ExceptionHandler(UserMessageException.class)
    public UserMessage handleUserMessageException(UserMessageException exception) {
        return UserMessage.error(exception.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public UserMessage handleBindException(BindException exception) {
        String message = exception.getFieldError().getDefaultMessage();
        return UserMessage.error(message);
    }
}

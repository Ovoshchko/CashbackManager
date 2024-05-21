package ru.cashbackManager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.cashbackManager.exception.*;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(value = {BankAlreadyExistsException.class, CardAlreadyExistsException.class})
    protected ResponseEntity<String> handleBankAlreadyExistsException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(value =
            {BankDoNotExistsException.class, CardDoNotExistsException.class,
                    CategoryDoNotExistsException.class, NoCategoryForCardException.class,
                    NoCardForCashbackException.class})
    protected ResponseEntity<String> handleBankDoNotExistsException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}

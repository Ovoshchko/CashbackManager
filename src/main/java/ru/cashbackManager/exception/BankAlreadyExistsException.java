package ru.cashbackManager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BankAlreadyExistsException extends RuntimeException {

    public BankAlreadyExistsException() {
        super("Банк был уже зарегистрирован");
    }
}

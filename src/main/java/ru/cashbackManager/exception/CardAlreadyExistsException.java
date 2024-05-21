package ru.cashbackManager.exception;

public class CardAlreadyExistsException extends RuntimeException {

    public CardAlreadyExistsException() {
        super("Карта уже существует.");
    }
}

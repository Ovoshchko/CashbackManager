package ru.cashbackManager.exception;

public class NoCategoryForCardException extends RuntimeException {

    public NoCategoryForCardException() {
        super("Для данной карты не существует такой категории");
    }
}

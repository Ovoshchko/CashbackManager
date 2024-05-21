package ru.cashbackManager.exception;

public class CategoryDoNotExistsException extends RuntimeException {

    public CategoryDoNotExistsException() {
        super("Такой категории пока не существует");
    }
}

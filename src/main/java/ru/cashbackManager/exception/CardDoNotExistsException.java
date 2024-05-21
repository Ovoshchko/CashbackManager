package ru.cashbackManager.exception;

public class CardDoNotExistsException extends RuntimeException {

    public CardDoNotExistsException() {
        super("Карта не была найдена. Зарегистрируйте ее или проверьте, что правильно ввели название");
    }
}

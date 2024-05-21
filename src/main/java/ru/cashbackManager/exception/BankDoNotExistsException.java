package ru.cashbackManager.exception;

public class BankDoNotExistsException extends RuntimeException {

    public BankDoNotExistsException() {
        super("Банка не существует. Попробуйте добавить его или проверить, что вы корректно ввели данные");
    }
}

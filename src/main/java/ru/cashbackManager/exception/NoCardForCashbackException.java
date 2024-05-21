package ru.cashbackManager.exception;

public class NoCardForCashbackException extends RuntimeException {

    public NoCardForCashbackException() {
        super("Боюсь, что нет доступного кешбека для");
    }
}

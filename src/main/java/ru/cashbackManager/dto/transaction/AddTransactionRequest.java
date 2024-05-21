package ru.cashbackManager.dto.transaction;

import lombok.Data;

@Data
public class AddTransactionRequest {

    private String cardName;

    private String categoryName;

    private int amount;
}

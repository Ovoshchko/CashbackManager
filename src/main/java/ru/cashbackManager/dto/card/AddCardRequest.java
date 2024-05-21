package ru.cashbackManager.dto.card;

import lombok.Data;

@Data
public class AddCardRequest {

    private String bankName;

    private String cardName;
}

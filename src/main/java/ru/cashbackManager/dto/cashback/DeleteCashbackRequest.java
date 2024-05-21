package ru.cashbackManager.dto.cashback;

import lombok.Data;

@Data
public class DeleteCashbackRequest {

    private String cardName;

    private String categoryName;
}

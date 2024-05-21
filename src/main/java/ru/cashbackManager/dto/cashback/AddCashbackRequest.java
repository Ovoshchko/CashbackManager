package ru.cashbackManager.dto.cashback;

import lombok.Data;

@Data
public class AddCashbackRequest {

    private String cardName;

    private String categoryName;

    private double percent;

    private boolean permanent;
}

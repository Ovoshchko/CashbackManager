package ru.cashbackManager.dto.bank;

import lombok.Data;

@Data
public class AddBankRequest {

    private String bankName;

    private int limit;
}

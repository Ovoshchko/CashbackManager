package ru.cashbackManager.dto.cashback;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class CardWithCashbacksResponse {

    private String name;

    Map<String, Double> cashbacks;
}

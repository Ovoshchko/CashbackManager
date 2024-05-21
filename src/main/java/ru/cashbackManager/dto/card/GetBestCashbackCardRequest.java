package ru.cashbackManager.dto.card;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetBestCashbackCardRequest {

    private String categoryName;

    private Integer amount;
}

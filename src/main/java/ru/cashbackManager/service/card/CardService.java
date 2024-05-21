package ru.cashbackManager.service.card;

import org.springframework.transaction.annotation.Transactional;
import ru.cashbackManager.dto.card.AddCardRequest;
import ru.cashbackManager.model.Card;

public interface CardService {

    @Transactional
    Card addCard(AddCardRequest request);

    Card findCardByName(String name);
}

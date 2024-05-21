package ru.cashbackManager.service.card;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.cashbackManager.dto.card.AddCardRequest;
import ru.cashbackManager.exception.BankDoNotExistsException;
import ru.cashbackManager.exception.CardAlreadyExistsException;
import ru.cashbackManager.exception.CardDoNotExistsException;
import ru.cashbackManager.model.Bank;
import ru.cashbackManager.model.Card;
import ru.cashbackManager.repository.CardRepository;
import ru.cashbackManager.service.bank.BankService;

@Service
@RequiredArgsConstructor
public class CardCashbackService implements CardService {

    private final CardRepository cardRepository;
    private final BankService bankService;

    @Override
    public Card addCard(AddCardRequest request) {
        Bank bank = bankService.findBankByName(request.getBankName());

        if (bank == null) {
            throw new BankDoNotExistsException();
        }

        try {
            return cardRepository.saveAndFlush(
                    new Card().setBank(bank).setName(request.getCardName())
            );
        } catch (DataIntegrityViolationException exception) {
            throw new CardAlreadyExistsException();
        }
    }

    @Override
    public Card findCardByName(String name) {
        return cardRepository.findByName(name).orElseThrow(CardDoNotExistsException::new);
    }
}

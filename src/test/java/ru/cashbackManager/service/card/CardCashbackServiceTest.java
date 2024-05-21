package ru.cashbackManager.service.card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.cashbackManager.dto.card.AddCardRequest;
import ru.cashbackManager.exception.BankDoNotExistsException;
import ru.cashbackManager.exception.CardAlreadyExistsException;
import ru.cashbackManager.exception.CardDoNotExistsException;
import ru.cashbackManager.model.Bank;
import ru.cashbackManager.model.Card;
import ru.cashbackManager.repository.CardRepository;
import ru.cashbackManager.service.bank.BankService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardCashbackServiceTest {

    private static final String TEST_BANK_NAME = "Test Bank";
    private static final String NON_EXISTENT_BANK_NAME = "Non-existent Bank";
    private static final String TEST_CARD_NAME = "Test Card";
    private static final String NON_EXISTENT_CARD_NAME = "Non-existent Card";

    @Mock
    private CardRepository cardRepository;

    @Mock
    private BankService bankService;

    @InjectMocks
    private CardCashbackService cardCashbackService;

    @Test
    void addCard_Success() {
        AddCardRequest request = new AddCardRequest();
        request.setBankName(TEST_BANK_NAME);
        request.setCardName(TEST_CARD_NAME);

        Bank bank = new Bank();
        bank.setName(TEST_BANK_NAME);

        when(bankService.findBankByName(anyString())).thenReturn(bank);
        when(cardRepository.saveAndFlush(any(Card.class))).thenReturn(new Card().setBank(bank).setName(TEST_CARD_NAME));

        Card result = cardCashbackService.addCard(request);

        assertNotNull(result);
        assertEquals(TEST_CARD_NAME, result.getName());
        assertEquals(TEST_BANK_NAME, result.getBank().getName());

        verify(bankService, times(1)).findBankByName(anyString());
        verify(cardRepository, times(1)).saveAndFlush(any(Card.class));
    }

    @Test
    void addCard_BankNotExists() {
        AddCardRequest request = new AddCardRequest();
        request.setBankName(NON_EXISTENT_BANK_NAME);
        request.setCardName(TEST_CARD_NAME);

        when(bankService.findBankByName(anyString())).thenThrow(new BankDoNotExistsException());

        assertThrows(BankDoNotExistsException.class, () -> cardCashbackService.addCard(request));

        verify(bankService, times(1)).findBankByName(anyString());
        verify(cardRepository, times(0)).saveAndFlush(any(Card.class));
    }

    @Test
    void addCard_CardAlreadyExists() {
        AddCardRequest request = new AddCardRequest();
        request.setBankName(TEST_BANK_NAME);
        request.setCardName(TEST_CARD_NAME);

        Bank bank = new Bank();
        bank.setName(TEST_BANK_NAME);

        when(bankService.findBankByName(anyString())).thenReturn(bank);
        when(cardRepository.saveAndFlush(any(Card.class))).thenThrow(new DataIntegrityViolationException(""));

        assertThrows(CardAlreadyExistsException.class, () -> cardCashbackService.addCard(request));

        verify(bankService, times(1)).findBankByName(anyString());
        verify(cardRepository, times(1)).saveAndFlush(any(Card.class));
    }

    @Test
    void findCardByName_Success() {
        when(cardRepository.findByName(anyString())).thenReturn(Optional.of(new Card().setName(TEST_CARD_NAME)));

        Card result = cardCashbackService.findCardByName(TEST_CARD_NAME);

        assertNotNull(result);
        assertEquals(TEST_CARD_NAME, result.getName());

        verify(cardRepository, times(1)).findByName(anyString());
    }

    @Test
    void findCardByName_NotExists() {
        when(cardRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(CardDoNotExistsException.class, () -> cardCashbackService.findCardByName(NON_EXISTENT_CARD_NAME));

        verify(cardRepository, times(1)).findByName(anyString());
    }
}

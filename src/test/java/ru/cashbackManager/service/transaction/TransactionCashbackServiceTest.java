package ru.cashbackManager.service.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.cashbackManager.dto.transaction.AddTransactionRequest;
import ru.cashbackManager.exception.NoCategoryForCardException;
import ru.cashbackManager.model.*;
import ru.cashbackManager.repository.TransactionRepository;
import ru.cashbackManager.service.bank.BankService;
import ru.cashbackManager.service.cashback.CashbackService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TransactionCashbackServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CashbackService cashbackService;

    @Mock
    private BankService bankService;

    @InjectMocks
    private TransactionCashbackService transactionCashbackService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addTransaction_Success() {
        AddTransactionRequest request = new AddTransactionRequest();
        request.setCardName("Test Card");
        request.setCategoryName("Test Category");
        request.setAmount(1000);

        Card card = new Card();
        card.setName("Test Card");
        Category category = new Category();
        category.setName("Test Category");
        Cashback cashback = new Cashback();
        cashback.setCard(card);
        cashback.setCategory(category);

        when(cashbackService.getCashbackByCardNameAndCategory(anyString(), anyString())).thenReturn(cashback);
        when(transactionRepository.saveAndFlush(any(Transaction.class))).thenReturn(new Transaction());

        Transaction result = transactionCashbackService.addTransaction(request);

        assertNotNull(result);
        verify(cashbackService, times(1)).getCashbackByCardNameAndCategory(anyString(), anyString());
        verify(transactionRepository, times(1)).saveAndFlush(any(Transaction.class));
    }

    @Test
    void addTransaction_NoCategoryForCardException() {
        AddTransactionRequest request = new AddTransactionRequest();
        request.setCardName("Test Card");
        request.setCategoryName("Test Category");
        request.setAmount(1000);

        when(cashbackService.getCashbackByCardNameAndCategory(anyString(), anyString())).thenReturn(null);

        assertThrows(NoCategoryForCardException.class, () -> transactionCashbackService.addTransaction(request));

        verify(cashbackService, times(1)).getCashbackByCardNameAndCategory(anyString(), anyString());
        verify(transactionRepository, times(0)).saveAndFlush(any(Transaction.class));
    }

    @Test
    void getTransactionsForBankCurrentMonth_Success() {
        Bank bank = new Bank();
        bank.setName("Test Bank");
        bank.setLimitAmount(10000L);

        Card card = new Card();
        card.setBank(bank);

        Transaction transaction = new Transaction();
        transaction.setCard(card);
        transaction.setAmount(5000);

        when(transactionRepository.findTransactionsByMonth(anyInt(), anyInt())).thenReturn(List.of(transaction));
        when(bankService.getAllBanks()).thenReturn(List.of(bank));

        Map<String, Long> result = transactionCashbackService.getTransactionsForBankCurrentMonth();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(5000L, result.get("Test Bank"));

        verify(transactionRepository, times(1)).findTransactionsByMonth(anyInt(), anyInt());
        verify(bankService, times(1)).getAllBanks();
    }

    @Test
    void getTransactionsForBankCurrentMonth_LimitExceeded() {
        Bank bank = new Bank();
        bank.setName("Test Bank");
        bank.setLimitAmount(5000L);

        Card card = new Card();
        card.setBank(bank);

        Transaction transaction = new Transaction();
        transaction.setCard(card);
        transaction.setAmount(6000);

        when(transactionRepository.findTransactionsByMonth(anyInt(), anyInt())).thenReturn(List.of(transaction));
        when(bankService.getAllBanks()).thenReturn(List.of(bank));

        Map<String, Long> result = transactionCashbackService.getTransactionsForBankCurrentMonth();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(0L, result.get("Test Bank"));

        verify(transactionRepository, times(1)).findTransactionsByMonth(anyInt(), anyInt());
        verify(bankService, times(1)).getAllBanks();
    }
}

package ru.cashbackManager.service.bank;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.cashbackManager.dto.bank.AddBankRequest;
import ru.cashbackManager.exception.BankDoNotExistsException;
import ru.cashbackManager.model.Bank;
import ru.cashbackManager.repository.BankRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankCashbackServiceTest {

    private static final String BANK_NAME = "Test Bank";
    private static final int LIMIT_AMOUNT = 1;
    @Mock
    private BankRepository bankRepository;

    @InjectMocks
    private BankCashbackService bankCashbackService;

    @Test
    void addBank_Success() {
        AddBankRequest request = new AddBankRequest();
        request.setBankName(BANK_NAME);
        request.setLimit(LIMIT_AMOUNT);

        when(bankRepository.saveAndFlush(any(Bank.class))).thenReturn(new Bank().setName(BANK_NAME).setLimitAmount(LIMIT_AMOUNT));

        Bank result = bankCashbackService.addBank(request);

        assertNotNull(result);
        assertEquals(BANK_NAME, result.getName());
        assertEquals(LIMIT_AMOUNT, result.getLimitAmount());

        verify(bankRepository, times(1)).saveAndFlush(any(Bank.class));
    }

    @Test
    void findBankByName_Exists() {
        when(bankRepository.findByName(anyString())).thenReturn(Optional.of(new Bank()));

        Bank result = bankCashbackService.findBankByName("Test Bank");

        assertNotNull(result);

        verify(bankRepository, times(1)).findByName(anyString());
    }

    @Test
    void findBankByName_NotExists() {
        when(bankRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(BankDoNotExistsException.class, () -> {
            bankCashbackService.findBankByName("Test Bank");
        });

        verify(bankRepository, times(1)).findByName(anyString());
    }

    @Test
    void getAllBanks() {
        List<Bank> banks = List.of(new Bank(), new Bank());

        when(bankRepository.findAll()).thenReturn(banks);

        List<Bank> result = bankCashbackService.getAllBanks();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(bankRepository, times(1)).findAll();
    }
}

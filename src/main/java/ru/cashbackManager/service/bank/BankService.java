package ru.cashbackManager.service.bank;

import org.springframework.transaction.annotation.Transactional;
import ru.cashbackManager.dto.bank.AddBankRequest;
import ru.cashbackManager.model.Bank;

import java.util.List;

public interface BankService {

    @Transactional
    Bank addBank(AddBankRequest request);

    Bank findBankByName(String bankName);

    List<Bank> getAllBanks();
}

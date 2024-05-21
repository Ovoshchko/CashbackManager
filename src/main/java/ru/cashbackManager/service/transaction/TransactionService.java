package ru.cashbackManager.service.transaction;

import org.springframework.transaction.annotation.Transactional;
import ru.cashbackManager.dto.transaction.AddTransactionRequest;
import ru.cashbackManager.model.Transaction;

import java.util.Map;

public interface TransactionService {
    @Transactional
    Transaction addTransaction(AddTransactionRequest request);

    Map<String, Long> getTransactionsForBankCurrentMonth();
}

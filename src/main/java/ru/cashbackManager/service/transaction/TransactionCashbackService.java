package ru.cashbackManager.service.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cashbackManager.dto.transaction.AddTransactionRequest;
import ru.cashbackManager.exception.NoCategoryForCardException;
import ru.cashbackManager.model.Bank;
import ru.cashbackManager.model.Cashback;
import ru.cashbackManager.model.Transaction;
import ru.cashbackManager.repository.TransactionRepository;
import ru.cashbackManager.service.bank.BankService;
import ru.cashbackManager.service.cashback.CashbackService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionCashbackService implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CashbackService cashbackService;
    private final BankService bankService;

    @Override
    public Transaction addTransaction(AddTransactionRequest request) {
        Cashback cardCashback =
                cashbackService.getCashbackByCardNameAndCategory(request.getCardName(), request.getCategoryName());

        if (cardCashback == null) {
            throw new NoCategoryForCardException();
        }

        return transactionRepository.saveAndFlush(
                new Transaction().setCard(cardCashback.getCard())
                        .setCategory(cardCashback.getCategory())
                        .setAmount(request.getAmount())
                        .setTransactionDate(LocalDateTime.now())
        );
    }

    @Override
    public Map<String, Long> getTransactionsForBankCurrentMonth() {
        List<Transaction> transactions = transactionRepository.findTransactionsByMonth(
                LocalDateTime.now().getMonthValue(),
                LocalDateTime.now().getYear()
        );

        Map<String, Long> totalTransactionsByBank = transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getCard().getBank().getName(),
                        Collectors.summingLong(Transaction::getAmount)
                ));

        Map<String, Long> cashbackLimitsByBank = bankService.getAllBanks().stream()
                .collect(Collectors.toMap(
                        Bank::getName,
                        Bank::getLimitAmount
                ));

        return cashbackLimitsByBank.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> Math.max(
                                entry.getValue() - totalTransactionsByBank.getOrDefault(entry.getKey(), 0L), 0L)
                ));
    }
}

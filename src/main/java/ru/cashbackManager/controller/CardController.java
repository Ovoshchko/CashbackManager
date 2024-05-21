package ru.cashbackManager.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cashbackManager.dto.card.AddCardRequest;
import ru.cashbackManager.dto.card.GetBestCashbackCardRequest;
import ru.cashbackManager.dto.cashback.CardWithCashbacksResponse;
import ru.cashbackManager.dto.transaction.AddTransactionRequest;
import ru.cashbackManager.service.card.CardService;
import ru.cashbackManager.service.cashback.CashbackService;
import ru.cashbackManager.service.category.CategoryService;
import ru.cashbackManager.service.transaction.TransactionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final CashbackService cashbackService;
    private final TransactionService transactionService;
    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Добавить карту", description = "Добавляет карту в хранилище")
    public ResponseEntity<Void> addCard(@RequestBody AddCardRequest addCardRequest) {
        cardService.addCard(addCardRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Показать все карты с кешбеком", description = "Возвращает все карты, у которых есть кешбеки")
    public ResponseEntity<List<CardWithCashbacksResponse>> getCardsWithCashbacks() {
        return ResponseEntity.ok().body(cashbackService.getAllCashbacks());
    }

    @PostMapping("/transaction")
    @Operation(summary = "Добавить транзакцию", description = "Добавляет транзакцию в хранилище")
    public ResponseEntity<Void> addTransaction(@RequestBody AddTransactionRequest addTransactionRequest) {
        transactionService.addTransaction(addTransactionRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category")
    @Operation(summary = "Выбрать карту для оплаты",
            description = "Ищет лучший вариант среди карт с кешбеком на данную категорию либо с категорией остальные")
    public ResponseEntity<String> getBestCashbackCard(@RequestParam("categoryName") String categoryName,
                                                      @RequestParam(value = "amount", required = false) Integer amount) {
        GetBestCashbackCardRequest request = new GetBestCashbackCardRequest()
                .setCategoryName(categoryName)
                .setAmount(amount);
        String bestCard = categoryService.getBestCard(request);
        return ResponseEntity.ok().body(bestCard);
    }

    @GetMapping("/estimate")
    @Operation(summary = "Оценить кешбек", description = "Считает остаток по кешбеку для всех банков")
    public ResponseEntity<Map<String, Long>> estimateCashback() {
        return ResponseEntity.ok().body(transactionService.getTransactionsForBankCurrentMonth());
    }

}

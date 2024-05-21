package ru.cashbackManager.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cashbackManager.dto.cashback.AddCashbackRequest;
import ru.cashbackManager.dto.cashback.DeleteCashbackRequest;
import ru.cashbackManager.service.cashback.CashbackService;

@RestController
@RequestMapping("/cashback")
@RequiredArgsConstructor
public class CashbackController {

    private final CashbackService cashbackService;

    @PostMapping("/current")
    @Operation(summary = "Добавить кешбек на текущий месяц",
            description = "Добавляет кешбек со сроком до конца текущего месяца")
    public ResponseEntity<Void> addCurrentCashback(@RequestBody AddCashbackRequest addCashbackRequest) {
        cashbackService.addCurrentCashback(addCashbackRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/future")
    @Operation(summary = "Добавить кешбек на следующий месяц",
            description = "Добавляет кешбек со сроком с первого числа следующего месяца до его конца")
    public ResponseEntity<Void> addFutureCashback(@RequestBody AddCashbackRequest addCashbackRequest) {
        cashbackService.addFutureCashback(addCashbackRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/current")
    @Operation(summary = "Удалить кешбек на текущий месяц",
            description = "Удаляет кешбек со сроком до конца текущего месяца")
    public ResponseEntity<Void> deleteCurrentCashback(@RequestBody DeleteCashbackRequest deleteCashbackRequest) {
        cashbackService.deleteCurrentCashback(deleteCashbackRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/future")
    @Operation(summary = "Удалить кешбек на следующий месяц",
            description = "Удаляет кешбек со сроками следующего месяца месяца")
    public ResponseEntity<Void> deleteFutureCashback(@RequestBody DeleteCashbackRequest deleteCashbackRequest) {
        cashbackService.deleteFutureCashback(deleteCashbackRequest);
        return ResponseEntity.ok().build();
    }

}

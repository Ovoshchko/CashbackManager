package ru.cashbackManager.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cashbackManager.dto.bank.AddBankRequest;
import ru.cashbackManager.service.bank.BankService;

@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @PostMapping()
    @Operation(summary = "Добавить банк", description = "Добавляет банк в хранилище")
    public ResponseEntity<Void> addBank(@RequestBody AddBankRequest addBankRequest) {
        bankService.addBank(addBankRequest);
        return ResponseEntity.ok().build();
    }
}

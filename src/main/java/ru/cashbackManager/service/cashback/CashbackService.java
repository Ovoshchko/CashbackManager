package ru.cashbackManager.service.cashback;

import org.springframework.transaction.annotation.Transactional;
import ru.cashbackManager.dto.cashback.AddCashbackRequest;
import ru.cashbackManager.dto.cashback.CardWithCashbacksResponse;
import ru.cashbackManager.dto.cashback.DeleteCashbackRequest;
import ru.cashbackManager.model.Cashback;

import java.util.List;

public interface CashbackService {

    @Transactional
    Cashback addCurrentCashback(AddCashbackRequest request);

    @Transactional
    Cashback addFutureCashback(AddCashbackRequest request);

    @Transactional
    void deleteCurrentCashback(DeleteCashbackRequest deleteCashbackRequest);

    @Transactional
    void deleteFutureCashback(DeleteCashbackRequest deleteCashbackRequest);

    List<CardWithCashbacksResponse> getAllCashbacks();

    Cashback getCashbackByCardNameAndCategory(String cardName, String categoryName);
}
